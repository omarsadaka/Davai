package com.technosaab.newdavai.rest;

import com.technosaab.newdavai.Models.AboutApp;
import com.technosaab.newdavai.Models.AddFavResponse;
import com.technosaab.newdavai.Models.AddFavourite;
import com.technosaab.newdavai.Models.AdsResponse;
import com.technosaab.newdavai.Models.BookingInfo;
import com.technosaab.newdavai.Models.BookingServiceResponse;
import com.technosaab.newdavai.Models.CategoryClientResponse;
import com.technosaab.newdavai.Models.CategoryResponse;
import com.technosaab.newdavai.Models.CategoryService;
import com.technosaab.newdavai.Models.ChatHistoryItem;
import com.technosaab.newdavai.Models.CityModel;
import com.technosaab.newdavai.Models.ClientAds;
import com.technosaab.newdavai.Models.ClientBooking;
import com.technosaab.newdavai.Models.ClientChat;
import com.technosaab.newdavai.Models.ClientInfo;
import com.technosaab.newdavai.Models.ClientRate;
import com.technosaab.newdavai.Models.ClientResponse;
import com.technosaab.newdavai.Models.ClientServiceResponse2;
import com.technosaab.newdavai.Models.ContactUs;
import com.technosaab.newdavai.Models.CountryModel;
import com.technosaab.newdavai.Models.GetVideo;
import com.technosaab.newdavai.Models.NotificationResponse;
import com.technosaab.newdavai.Models.NumRes;
import com.technosaab.newdavai.Models.Ratting;
import com.technosaab.newdavai.Models.SettingResponse;
import com.technosaab.newdavai.Models.StartChatMessage;
import com.technosaab.newdavai.Models.StartChatResponse;
import com.technosaab.newdavai.Models.TermsModel;
import com.technosaab.newdavai.Models.UpdateBookingInfo;
import com.technosaab.newdavai.Models.User;
import com.technosaab.newdavai.Models.UserByIdResponse;
import com.technosaab.newdavai.Models.UserChatHistory;
import com.technosaab.newdavai.Models.UserContact;
import com.technosaab.newdavai.Models.UserReservResponse;
import com.technosaab.newdavai.Models.UserResponse;
import com.technosaab.newdavai.Models.Vendor;
import com.technosaab.newdavai.Models.userInfo;
import com.technosaab.newdavai.Models.FavouriteResponse;
import java.util.List;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ClientInterface {

    @POST("register")
    Call<UserResponse> addNewUser(@Body userInfo userInfo);

    @POST("client")
    Call<ClientResponse> addNewVendor(@Body ClientInfo clientInfo);

    @GET("login")
    Call<User> userLogIn(@Query("val") String value,
                         @Query("password") String password ,
                         @Query("userKey") String userKey);
    @GET("countries")
    Call<List<CountryModel>> getCountries();

    @GET("citiesById")
    Call<List<CityModel>> getCities(@Query("countryID") String id);

    @GET("userAbout")
    Call<List<AboutApp>> getUserAbout();

    @GET("clientAbout")
    Call<List<AboutApp>> getClientAbout();

    @GET("getUserByID")
    Call<UserByIdResponse> getUserById(@Query("id") String id);

    @GET("Category")
    Call<List<CategoryResponse>> getAllCategories();

    @GET("servicesByCategoryID")
    Call<List<CategoryService>> getCategoryService(@Query("categoryID") String id);

    @Multipart
    @POST("uploadFile")
    Call<ResponseBody> uploadFile(@Part MultipartBody.Part image);

    @GET("getClientByID")
    Call<Vendor> getVendorAbout(@Query("id") String id );

    @GET("getClientByID")
    Call<Vendor> GetVendorAbout(@Query("id") String id , @Query("userID") String userId);

    @POST("userFav")
    Call<AddFavResponse> addFavourite(@Body AddFavourite addFavourite);

    @GET("userFav")
    Call<List<FavouriteResponse>> getFavourite(@Query("id") String id);

    @GET("deleteFav")
    Call<ResponseBody> deleteFavourite(@Query("userID") String userId , @Query("clientID") String clientId);

    @POST("contactUS")
    Call<UserContact> addContactUS(@Body ContactUs contactUs);

    @GET("forgetPassword")
    Call<ResponseBody> forgetPassword(@Query("email") String email);

    @GET("userTerms")
    Call<List<TermsModel>> getUserTerms();

    @GET("clientTerms")
    Call<List<TermsModel>> getClientTerms();

//    @POST("addNewClientPerson")
//    Call<Integer> addNewClientPerson(@Field("title") String title ,
//                                     @Field("clientID") int clientId);
    @PUT("user/{id}/")
    Call<ResponseBody> updateUser(@Path("id") String userId, @Body userInfo updateUser);

    @GET("clientByCategoryID")
    Call<List<CategoryClientResponse>> getCategoryClient(@Query("categoryID") String id);

    @GET("clientServices")
    Call<List<ClientServiceResponse2>> getClientService(@Query("clientID") String id);

    @POST("booking")
    Call<ResponseBody> booking(@Body BookingInfo bookingInfo);

    @GET("clients")
    Call<List<ClientResponse>> getAllClient();


    @GET("getChatHistory")
    Call<List<ChatHistoryItem>> getChatHistory(@Query("userID") String userID, @Query("clientID") String clientID);

    @GET("getClientChat")
    Call<List<ClientChat>> getClientChat(@Query("clientID") String clientID);

    @GET("getUserBooking")
    Call<List<UserReservResponse>> getUserBooking(@Query("userID") String id);

    @GET("getBookingServices")
    Call<List<BookingServiceResponse>> getBookingService(@Query("bookingID") String id);

    @PUT("client/{id}/")
    Call<ClientResponse> updateClient(@Path("id") String ClientId, @Body ClientInfo clientInfo);

    @POST("updateBooking")
    Call<ResponseBody> updateBooking(@Body UpdateBookingInfo bookingInfo);

    @POST("updateClientBooking")
    Call<ResponseBody> updateClientBooking(@Body UpdateBookingInfo bookingInfo);

    @POST("updateClientServices")
    Call<ResponseBody> updateClientServices(@Body ClientServiceResponse2 clientServiceResponse2);

    @POST("startChat")
    Call<StartChatResponse> startChat(@Body StartChatMessage startChatMessage);

    @GET("userCancelBooking")
    Call<ResponseBody> deleteBooking(@Query("id") String bookingId , @Query("userID") String userId);

    @GET("clientCancelBooking")
    Call<ResponseBody> clientCancelBooking(@Query("id") String bookingId , @Query("clientID") String clientId);





    @GET("getUserChat")
    Call<List<UserChatHistory>> getUserChat(@Query("userID") String userID);

    @POST("clientAds")
    Call<AdsResponse> clientAds(@Body ClientAds clientAds);

    @GET("getUserByEmail")
    Call<UserByIdResponse> getUserByEmail(@Query("email") String email);

    @GET("adsVideo")
    Call<List<GetVideo>> adsVideo();

//    @POST("setting")
//    Call<AdsResponse> setting(@Body Setting setting);

    @GET("setting")
    Call<List<SettingResponse>> setting();

    @POST("ratePlace")
    Call<Object> addRatting(@Body Ratting ratting);

    @GET("totalClientFav")
    Call<NumRes> totalClientFav(@Query("clientID") String clientID);

    @GET("getClientBooking")
    Call<List<ClientBooking>> getClientBooking(@Query("clientID") String userID);

    @GET("userNotify")
    Call<List<NotificationResponse>> getUserNotify(@Query("id") String userID);

    @GET("clientNotify")
    Call<List<NotificationResponse>> getClientNotify(@Query("id") String ClientID);

    @GET("getClientRate")
    Call<List<ClientRate>> getClientRate(@Query("clientID") String ClientID);
}
