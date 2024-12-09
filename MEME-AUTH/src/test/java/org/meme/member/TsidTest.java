package org.meme.member;

import io.hypersistence.tsid.TSID;
import org.junit.jupiter.api.Test;

import java.time.Instant;

public class TsidTest {

    @Test
    void TSID_TEST() {
        long aLong = TSID.fast().toLong();
        System.out.println("aLong = " + aLong);

        long bLong = TSID.fast().toLong();
        System.out.println("bLong = " + bLong);

        Instant instant = TSID.fast().getInstant();
        System.out.println("instant = " + instant);
    }
}
