package mr.bel.projectforhttpre.utils;
import java.util.Map;
import retrofit.Call;
import retrofit.http.FieldMap;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

public interface Link {
    @FormUrlEncoded
    @POST("method/users.get")
    Call<Object> ParsVK(@FieldMap Map<String,String> map);
}
