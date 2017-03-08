package main.helpers;

import retrofit.client.Response;

public class ApiResponseHelper
{

    private static final int RESPONSE_CODE_OK = 200;

    public static boolean checkSuccessStatusCode(Response response) {
        return response.getStatus() == RESPONSE_CODE_OK;
    }

}
