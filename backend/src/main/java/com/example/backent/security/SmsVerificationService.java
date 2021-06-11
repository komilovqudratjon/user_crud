package com.example.backent.security;

import com.example.backent.payload.ApiResponseModel;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Formatter;

@Service
public class SmsVerificationService {
    public ApiResponseModel sendEms(String phoneNumber, String code) {
        StringBuilder builder = new StringBuilder();
        TrustManager[] trustAllCertificates = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }

                    @Override
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }
                }
        };
        String url = "https://sms.filecloud.uz/gateway/index";
        StringBuilder value = new StringBuilder();
        String timeStamp = String.valueOf(System.currentTimeMillis());
        value.append("IFl0YFfgby_bbsnq8d8PsFwzgpJYRo1Y").append("-").append(encodeToSha1("2byMFBj%iU^1xn57QR@A8PKpGEWhD64s" + timeStamp)).append("-").append(timeStamp);
        String charset = java.nio.charset.StandardCharsets.UTF_8.name();
        try {
            HttpsURLConnection connection = (HttpsURLConnection) new URL(url).openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Accept-Charset", charset);
            connection.setRequestProperty("Auth", value.toString());
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            String json = "{\"method\": \"sendMes\",\"id\": 1,\"params\": {\"phone\": " + phoneNumber.substring(1) + ",\n\t\t\t\"text\":\"Verication code for WIKIQUICKY : " + code + "\"}}";

            try {
                SSLContext sslContext = SSLContext.getInstance("SSL");
                sslContext.init(null, trustAllCertificates, new java.security.SecureRandom());
                connection.setSSLSocketFactory(sslContext.getSocketFactory());
            } catch (NoSuchAlgorithmException | KeyManagementException e) {
                e.printStackTrace();
            }

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = json.getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            try (InputStream response = connection.getInputStream()) {
                int c = 0;
                while ((c = response.read()) != -1) {
                    builder.append((char) c);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ApiResponseModel(HttpStatus.OK.value(), "success");
    }

    public String encodeToSha1(String data) {
        String sha1 = "";
        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(data.getBytes(StandardCharsets.UTF_8));
            sha1 = byteToHex(crypt.digest());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sha1;
    }

    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }
}
