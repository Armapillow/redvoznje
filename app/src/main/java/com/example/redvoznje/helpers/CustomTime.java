package com.example.redvoznje.helpers;

// TODO: smisli bolje ime??
public abstract class CustomTime {

    public abstract long getTimeInMs();

    public static final class Fixed extends CustomTime {
        public final long timeMs;

        public Fixed(final long time) {
            this.timeMs = time;
        }

        @Override
        public long getTimeInMs() {
            return timeMs;
        }
    }


    public static final class Relative extends CustomTime {
        public final long timeMs;


        public Relative(final long time) {
            this.timeMs = time;
        }

        @Override
        public long getTimeInMs() {
            return timeMs;
        }


    }


    public static long now() {
        return System.currentTimeMillis();
    }
}
