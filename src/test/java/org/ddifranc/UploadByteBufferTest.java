package org.ddifranc;


import io.micronaut.buffer.netty.NettyByteBufferFactory;
import io.micronaut.core.io.buffer.ByteBuffer;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.MutableHttpRequest;
import io.micronaut.http.client.RxStreamingHttpClient;
import io.netty.buffer.ByteBuf;
import io.reactivex.Flowable;
import org.junit.jupiter.api.Test;

import java.net.URL;


public class UploadByteBufferTest {


    //fails w/ ReadTimeoutException
    @Test
    public void testPutByteBuffers() throws Exception {
        URL baseURL = new URL("http://httpbin.org");
        RxStreamingHttpClient rxclient = RxStreamingHttpClient.create(baseURL);

        String body = "body";
        Flowable<ByteBuffer<ByteBuf>> bufs = Flowable.just(
                NettyByteBufferFactory.DEFAULT.wrap(body.getBytes()));

        MutableHttpRequest<?> request = HttpRequest.PUT("/put", bufs)
                .contentType(MediaType.APPLICATION_OCTET_STREAM_TYPE);

        HttpResponse<String> result = rxclient.exchange(request, String.class).blockingSingle();
        System.out.println(result.body());

    }

    //passes
    @Test
    public void testPutByteBufs() throws Exception {
        URL baseURL = new URL("http://httpbin.org");
        RxStreamingHttpClient rxclient = RxStreamingHttpClient.create(baseURL);

        String body = "body";
        Flowable<ByteBuf> bufs = Flowable.just(
                NettyByteBufferFactory.DEFAULT.wrap(body.getBytes()))
                .map(ByteBuffer::asNativeBuffer);


        MutableHttpRequest<?> request = HttpRequest.PUT("/put", bufs)
                .contentType(MediaType.APPLICATION_OCTET_STREAM_TYPE);

        HttpResponse<String> result = rxclient.exchange(request, String.class).blockingSingle();
        System.out.println(result.body());

    }


    //passes
    @Test
    public void testPutByteArrays() throws Exception {
        URL baseURL = new URL("http://httpbin.org");
        RxStreamingHttpClient rxclient = RxStreamingHttpClient.create(baseURL);

        String body = "body";
        Flowable<byte[]> bufs = Flowable.just(
                NettyByteBufferFactory.DEFAULT.wrap(body.getBytes()))
                .map(ByteBuffer::toByteArray);


        MutableHttpRequest<?> request = HttpRequest.PUT("/put", bufs)
                .contentType(MediaType.APPLICATION_OCTET_STREAM_TYPE)
                .accept(MediaType.ALL_TYPE);

        HttpResponse<String> result = rxclient.exchange(request, String.class).blockingSingle();
        System.out.println(result.body());

    }

    //passes
    @Test
    public void testPutByteBuffers_NoContentType() throws Exception {
        URL baseURL = new URL("http://httpbin.org");
        RxStreamingHttpClient rxclient = RxStreamingHttpClient.create(baseURL);

        String body = "body";
        Flowable<ByteBuffer<ByteBuf>> bufs = Flowable.just(
                NettyByteBufferFactory.DEFAULT.wrap(body.getBytes()));

        MutableHttpRequest<?> request = HttpRequest.PUT("/put", bufs);

        HttpResponse<String> result = rxclient.exchange(request, String.class).blockingSingle();
        System.out.println(result.body());

    }

}
