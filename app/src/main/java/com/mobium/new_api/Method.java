package com.mobium.new_api;

import android.support.annotation.NonNull;

import com.annimon.stream.function.Predicate;
import com.mobium.new_api.models.MobiumError;
import com.mobium.new_api.models.Response;
import com.mobium.new_api.models.ResponseBase;
import com.mobium.reference.utils.Functional;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import retrofit.Callback;
import retrofit.RetrofitError;

/**
 *  on 19.06.15.
 * http://mobiumapps.com/
 */
public class Method<Arg, Data extends ResponseBase> {
    public static ExceptionHandler staticHandler;

    private final MethodInterface<Arg, Response<Data>> methodImplementation;
    private final Receiver<Data> internalReceiver;
    private final Arg methodArgument;

    private static final ScheduledExecutorService worker =
            Executors.newSingleThreadScheduledExecutor();



    public Method(
        MethodInterface<Arg, Response<Data>> methodImplementation,
        Arg methodArgument) {
            this.methodImplementation = methodImplementation;
            this.methodArgument = methodArgument;
            internalReceiver = null;
    }

    public Method(
            MethodInterface<Arg, Response<Data>> methodImplementation,
            Arg methodArgument,
            Receiver<Data> internalReceiver) {
        this.methodImplementation = methodImplementation;
        this.methodArgument = methodArgument;
        this.internalReceiver = internalReceiver;
    }



    public void invoke(@NonNull final Handler<Arg, Data> handler) {
        try {
            methodImplementation.call(methodArgument, new Callback<Response<Data>>() {
                @Override
                public void success(Response<Data> dataResponse, retrofit.client.Response response) {
                    if (dataResponse.success()) {
                        handler.onResult(dataResponse.result);
                        if (internalReceiver != null)
                            internalReceiver.onResult(dataResponse.result);
                    } else
                        handler.onBadArgument(dataResponse.error(), methodArgument);
                }

                @Override
                public void failure(RetrofitError error) {
                    error.printStackTrace();
                    handler.onException(error);
                    if (staticHandler!= null)
                        staticHandler.onExeption(error);
                }
            });
        } catch (Exception e) {
            handler.onException(e);
            if (staticHandler!= null)
                staticHandler.onExeption(e);
        }
    }


    /**
     * Invoke method without handling result and error.
     * @param predicate exception, after which the invoke is available
     * @param internalInvokeController callback to new Attempt
     */

    public void invokeWithPereatInvokeController(@NonNull Predicate<Exception> predicate,
                                                 @NonNull RepeatInvokesController internalInvokeController) {
        final Method<Arg, Data> method = this;


        final Handler<Arg, Data> handler = new Handler<Arg, Data>() {
            private int currentAttemp = 0;

            private void newAttempt() {
                currentAttemp++;
                worker.schedule(() -> method.invoke(this), 200, TimeUnit.MILLISECONDS);
            }

            @Override
            public void onResult(Data data) {
                //good
            }

            @Override
            public void onBadArgument(MobiumError mobiumError, Arg arg) {
                if (mobiumError.mayRetry && 10 > currentAttemp)
                    newAttempt();
            }

            @Override
            public void onException(Exception ex) {
                if (predicate.test(ex) && 10 > currentAttemp) {
                    internalInvokeController.onRequestNewInvoke();
                }
            }
        };

        internalInvokeController.supplier = () -> method.invoke(handler);

        invoke(handler);
    }

    /**
     * invoke Method without Handling result and errors
     * @param attemptCount number of attempts of invoking after repeatable error
     * @param delayMS invoke delay
     */

    public void invokeWithoutHandling(final int attemptCount, int delayMS) {
        final Method<Arg, Data> method = this;
        final Handler<Arg, Data> handler = new Handler<Arg, Data>() {
            int currentAttemp = 0;
            @Override
            public void onResult(Data data) {
                //good
            }
            private void newAttempt() {
                currentAttemp++;
                worker.schedule(() -> method.invoke(this), delayMS, TimeUnit.MILLISECONDS);
            }

            @Override
            public void onBadArgument(MobiumError mobiumError, Arg arg) {
                if (mobiumError.mayRetry && attemptCount > currentAttemp)
                    newAttempt();
            }

            @Override
            public void onException(Exception ex) {
                ex.printStackTrace();
                if (ex instanceof RetrofitError && attemptCount > currentAttemp) {
                    RetrofitError er = (RetrofitError) ex;
                    if (er.getKind() == RetrofitError.Kind.NETWORK)
                        newAttempt();
                }
            }
        };

        invoke(handler);
    }




    private Handler<Arg, Data> attemptHadler(Method<Arg, Data> method, final int attemptCount, int delayMS) {
        return new Handler<Arg, Data>() {
            int currentAttemp = 0;

            @Override
            public void onResult(Data data) {

            }

            @Override
            public void onBadArgument(MobiumError mobiumError, Arg arg) {
                if (currentAttemp < attemptCount) {
                    new android.os.Handler().postDelayed(() -> method.invoke(this), delayMS);
                }
            }

            @Override
            public void onException(Exception ex) {
                ex.printStackTrace();
            }
        };
    }

    public abstract class RepeatInvokesController {
        public RepeatInvokesController(Functional.Procedure supplier) {
            this.supplier = supplier;
        }

        public void invoke() {
            if (supplier != null)
                supplier.make();
        }

        public abstract void onRequestNewInvoke();


        public Functional.Procedure supplier;
    }

}
