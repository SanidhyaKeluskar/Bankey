package com.bankey.bankeyclient;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

/**
 * Created by DLutskov on 2/28/2018.
 */

public class SharingUtils {

    public static void shareFacebook(Activity activity) {
        ShareLinkContent content = new ShareLinkContent.Builder() // TODO
                .setContentUrl(Uri.parse("https://www.google.com/search?q=BankeyApp"))
                .build();
        ShareDialog.show(activity, content);
    }

    public static void shareTweeter(Activity activity) {
        String message = activity.getString(R.string.share_text);
        Uri image = resourceToUri(activity, R.drawable.logo_signup);

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, message);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_STREAM, image);
        intent.setType("image/jpeg");
        intent.setPackage("com.twitter.android");
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            // Open app
            activity.startActivity(intent);
        } else {
            // Open web
            Intent tweet = new Intent(Intent.ACTION_VIEW);
            tweet.setData(Uri.parse("http://twitter.com/?status=" + Uri.encode(message)));
            activity.startActivity(tweet);
        }
    }

    public static void shareWhatsapp(Activity activity) {
        String text = activity.getString(R.string.share_text);
        Uri bitmapUri = resourceToUri(activity, R.drawable.logo_signup);

        Intent shareIntent = new Intent();
        shareIntent.setPackage("com.whatsapp");
        shareIntent.setAction(Intent.ACTION_SEND);

        shareIntent.putExtra(Intent.EXTRA_TEXT, text);
        shareIntent.putExtra(Intent.EXTRA_STREAM, bitmapUri);
        shareIntent.setType("image/*");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        if (shareIntent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivity(Intent.createChooser(shareIntent, "Share Opportunity"));
        } else {
            Intent tweet = new Intent(Intent.ACTION_VIEW);
            tweet.setData(Uri.parse("https://api.whatsapp.com/send?text=" + Uri.encode(text)));
            activity.startActivity(tweet);
        }
    }

    public static Uri resourceToUri(Context context, int resID) {
        return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                context.getResources().getResourcePackageName(resID) + '/' +
                context.getResources().getResourceTypeName(resID) + '/' +
                context.getResources().getResourceEntryName(resID) );
    }

}
