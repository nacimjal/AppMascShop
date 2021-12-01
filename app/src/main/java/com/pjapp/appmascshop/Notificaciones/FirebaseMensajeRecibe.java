package com.pjapp.appmascshop.Notificaciones;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.pjapp.appmascshop.LoginActivity;
import com.pjapp.appmascshop.R;

public class FirebaseMensajeRecibe extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        //Notificacion via evento
        if(remoteMessage.getData().size() >0) {
            mostrarNotificacion(remoteMessage.getData().get("title"),remoteMessage.getData().get("message"));
        }

        //Notificacion
        if(remoteMessage.getNotification() != null){
            mostrarNotificacion(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
        }
    }

    private void mostrarNotificacion(String titulo, String mensaje) {
        Log.d("Mensaje=>","LLEgo aca");

        Intent intent = new Intent(this, LoginActivity.class);
        String canal_id = "web_app_channel";

        //Para que se muestre el mensaje en la parte superior
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        //Permite ingresar a la opción de notificaciones
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);

        //Tipo sonido que se va mostrar
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,canal_id)
                .setSmallIcon(R.drawable.icon_pedido)
                .setSound(uri)
                .setAutoCancel(true)
                .setVibrate(new long[]{1000,1000,1000,1000,1000})
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
            builder = builder.setContent(getNotificacionPersonalizada(titulo,mensaje));
            Log.d("Mensaje=>","Builder: "+builder);
        }else{
            builder = builder.setContentTitle(titulo)
                    .setContentText(mensaje)
                    .setSmallIcon(R.drawable.icon_pedido);
        }

        NotificationManager notificacion = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(canal_id,"web_app",NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setSound(uri,null);
            notificacion.createNotificationChannel(notificationChannel);
        }
        notificacion.notify(0,builder.build());
    }

    private RemoteViews getNotificacionPersonalizada(String titulo, String mensaje) {
        Log.d("Mensaje=>","Not personalizada: "+titulo+"-"+mensaje);

        RemoteViews remoteViews = new RemoteViews(this.getPackageName(),R.layout.notificacion_push);
        remoteViews.setTextViewText(R.id.txtTitulo,titulo);
        remoteViews.setTextViewText(R.id.txtMensaje,mensaje);
        remoteViews.setImageViewResource(R.id.idIcono,R.drawable.icon_pedido);
        return remoteViews;
    }
}
