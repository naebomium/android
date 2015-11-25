package com.mobium.reference.utils;

import com.annimon.stream.Objects;
import com.annimon.stream.Optional;
import com.annimon.stream.Stream;
import com.annimon.stream.function.BiFunction;
import com.annimon.stream.function.Consumer;
import com.annimon.stream.function.Function;
import com.annimon.stream.function.Predicate;
import com.annimon.stream.function.Supplier;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *  on 10.05.15.
 * http://mobiumapps.com/
 */
public final class Functional {

    @com.annimon.stream.function.FunctionalInterface
    public interface Requesting<T> {
        void requestData();
        void onResponse(T Data);
    }

    @com.annimon.stream.function.FunctionalInterface
    public interface Receiver<T> {
        void onReceive(T data);
    }

    @com.annimon.stream.function.FunctionalInterface
    public interface ChangeListener<T> {
        void onChange(T newValue);
    }


    /**
     * @return  optional.isPresent() ? optional.get() : option
     */
    public static <Result> Result optionalResult(Optional<Result> optional, Result option) {
        return optional.isPresent() ? optional.get() : option;
    }

    /**
     * @return lambda arg -> biPredicate (val) (map arg)
     */

    public static <Arg, Val> Predicate<Arg> biMapPredicate(Val val, Function<Arg, Val> map, BiFunction<Val, Val, Boolean> biPredicate) {
        return (Arg arg) -> biPredicate.apply(val, map.apply(arg));
    }

    /**
     * получить предикат вида map(Arg o) == compareWith
     * @param compareWith значение функции
     * @param map Arg -> Val функия
     * @param <Arg> тип, параметрезирующий получаемый предикат
     * @param <Val> тип, являющейся результатом функции над Arg
     * @return lambda arg -> Objects::equals (val) (map arg)
     */
    public static <Arg, Val> Predicate<Arg> equalsMapPredicate(Val compareWith, Function<Arg, Val> map) {
        return biMapPredicate(compareWith, map, Objects::equals);
    }

    /**
     * если в коллекции values есть элемент, удовлетворяющий predicate, вернуть его, иначе вернуть option
     * @param values коллекция
     * @param predicate предикат по которому производится поиск
     * @param option опция
     * @param <Result> тип возращаемого значения
     * @return результат поиска или option
     */

    public static <Result> Result optFind(List<Result> values,
                                          Predicate<Result> predicate,
                                          Result option) {
        return optionalResult(Stream.of(values).filter(predicate).findFirst(), option);
    }


    /**
     * Optional обертка для  json.getObject(i)
     */
    public final static BiFunction<Integer, JSONArray, Optional<JSONObject>> getOptionObject = (integer, array) -> {
        try {
            return Optional.of(array.getJSONObject(integer));
        } catch (JSONException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    };


    /**
     *  функция одного аргумента, бросающая JSONException
     * @param <ARG> тип аргумента функции
     * @param <RES> тип возвращаемого значения
     */
    @com.annimon.stream.function.FunctionalInterface
    public interface JSONFunction<ARG, RES> {
        RES apply(ARG t) throws  JSONException;
    }

    @com.annimon.stream.function.FunctionalInterface
    public interface JSONGetter<Res> {
        Res get() throws JSONException;
    }


    /**
     * фабрика функций парсинга JSONObject в <RES>
     * @param map   JSONObject -> RES throws JSONException
     * @param <RES> тип результата возращаемой функции
     * @return JSONObject -> Optional<T>
     */

    public static <RES> Function<JSONObject, Optional<RES>> createParser(JSONFunction<JSONObject, RES> map) {
        return object ->
        {
            try {
                return Optional.of(map.apply(object));
            } catch (JSONException e) {
                return Optional.empty();
            }
        };
    }


    /**
     * возращает Optional<Stream<T>> из JSONArray и функции JsonObject -> T
     * @param array Json array of T elements
     * @param map   JsonObject -> T throw JSONException
     * @param <T>   type argument
     * @return  (JSONArray, JSONObject -> T) -> Optional<Stream<T>>
     */

    public static <T> Optional<Stream<T>> optionalStream (Optional<JSONArray> array, JSONFunction<JSONObject, T> map) {
        return array.isPresent() ?
                Optional.of(
                Stream.ofRange(0, array.get().length())
                    .map(integer -> getOptionObject.apply(integer, array.get()))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .map(createParser(map))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                )
                : Optional.empty();
    }


    /**
     * @return ((Arg -> RES throw JSONFunction) arg) -> Optional<RES>
     */
    public static <RES, ARG> Optional<RES> createOptional (JSONFunction<ARG, RES> map,  ARG arg) {
        try {
            return Optional.of(map.apply(arg));
        } catch (JSONException e) {
            return Optional.empty();
        }
    }


    public static Optional<JSONArray> optionalJsonArray (JSONFunction<String, JSONArray> map, String arg) {
        return createOptional(map, arg);
    }


    /**
     * распарсить json array, если удалось его получить
     * @param getArray функуия получения JsonArray бросающая JsonException
     * @param arg  аргумент функции getArray
     * @param map  парсинг одного объекта массива в T
     * @param presentF применяется к потоку объектов T
     * @param <T> тип объекта
     */
    public static <T> void parseNullableJSONArray(JSONFunction<String, JSONArray> getArray, String arg, JSONFunction<JSONObject, T> map, Consumer<T> presentF) {
        optionalStream(
                optionalJsonArray(getArray, arg),
                map
        ).ifPresent(tStream -> tStream.forEach(presentF));
    }



    public static <T> Stream<T> parseJSON(JSONGetter<JSONArray> getter, JSONFunction<JSONObject, T> mapF) {
        try {
            JSONArray array = getter.get();
            return  Stream.ofRange(0, array.length())
                    .map(integer -> getOptionObject.apply(integer, array))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .map(createParser(mapF))
                    .filter(Optional::isPresent)
                    .map(Optional::get);
        } catch (JSONException e) {
            e.printStackTrace();
            return Stream.of(new ArrayList<>(0));
        } catch (Exception e) {
            e.printStackTrace();
            return  Stream.of(new ArrayList<>(0));
        }
    }



    public static <Result> Optional<Result> providedSupplier(Supplier<Boolean> condition, Supplier<Result> f) {
        return Optional.ofNullable(condition.get() ? f.get() : null);
    }


    public interface Procedure {
        void make();
    }

    public static <Result> Optional<Result> providedSupplier(
            Supplier<Boolean> condition,
            Supplier<Result> then,
            Runnable else_) {

        if (condition.get())
            return Optional.ofNullable(then.get());
        else_.run();
        return Optional.empty();
    }


    public static <T> void optionalChoice(Optional<T> optional, Consumer<T> ifPresent, Runnable elseProc) {
        if (optional.isPresent())
            ifPresent.accept(optional.get());
        else
            elseProc.run();
    }


    public static <T, C extends Collection<T>> Optional<C> notNullAndNotEmpty(C collection) {
        return collection == null || collection.size() == 0 ? Optional.empty() : Optional.of(collection);
    }


    public interface ThrowableSupplier <T, E extends Exception> {
        T get() throws E;
    }
}
