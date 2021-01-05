package com.htw.signature;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.client.utils.DateUtils;
import org.springframework.http.HttpMethod;

import java.util.Date;

/**
 * Created by htw on 2020/12/7.
 */
public class TestMain {
    public static void main(String[] args) {
        System.out.println(DigestUtils.md5Hex(""));
        System.out.println(HttpMethod.GET.toString());

        String date = "Mon, 07 Dec 2020 02:31:40 GMT";
        Date date1 = DateUtils.parseDate(date, new String[]{DateUtils.PATTERN_RFC1123});
        System.out.println(date1);
        System.out.println(new Date());
        System.out.println(date1.before(new Date()));

    }
}
