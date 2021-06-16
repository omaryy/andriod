package notitication;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface Apiservice {
    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAAYjVIJVY:APA91bFMx8yzS8h-MRHjq1Mizc-dpB56FZ3CZcgPbGsN4uLxvG0Cg1wJqYfkQz02e4082xh8EaNMgjyXLb6nWBeKIrWn4acKSXCCcaB7JsiOf7OOudlxN2ORaj38A975MQaXc6TnEWfC"
    })
    @POST("fcm/send")
    Call<Response> sendNotification(@Body Sender body) ;
}
