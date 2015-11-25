package com.mobium.reference.utils;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import com.annimon.stream.Optional;
import com.mobium.client.models.ShopCategory;
import com.mobium.client.models.ShopItem;

/**
 *  on 31.07.15.
 * http://mobiumapps.com/
 */
public class ShareUtils {
    private static final IShare share;

    static {
        share = new ShareBabadu();
    }


    public static void shareProduct(@NonNull Context context, @NonNull ShopItem item) {
        share.shareProduct(context, item);
    }

    public interface IShare {
        void shareProduct(@NonNull Context context, @NonNull ShopItem item);
        void shareCategory(@NonNull Context context, @NonNull ShopCategory category);
    }


    public static void shareCategory(@NonNull Context context, @NonNull ShopCategory category) {
        share.shareCategory(context, category);
    }


    private static class ShareBabadu implements IShare {

        @Override
        public void shareProduct(@NonNull Context context, @NonNull ShopItem item) {
            Optional<String> article = item.getRealId();
            String message = "Мне нравится " + item.title +
                    " за " + item.cost.getCurrentConst() + " руб.";
            if (article.isPresent()) {
                message += "http://babadu.ru/store/product/" + article.get();
            }

            shareMassage(context, message);
        }

        @Override
        public void shareCategory(@NonNull Context context, @NonNull ShopCategory category) {
            String marketingLabel = category.title;
            String link = "http://babadu.ru/";
            shareMassage(context, marketingLabel +"\n" + link);
        }

        private void shareMassage(@NonNull Context context, @NonNull String string) {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, string);
            context.startActivity(Intent.createChooser(sharingIntent, "Отправить через"));
        }
    }
}
