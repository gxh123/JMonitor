package com.jmonitor.core.status.model;

public class ThreadsInfo {
   private int count;
   private int peekCount;

   public ThreadsInfo() {
   }

   public int getCount() {
      return count;
   }

   public int getPeekCount() {
      return peekCount;
   }

   public ThreadsInfo setCount(int count) {
      this.count = count;
      return this;
   }

   public ThreadsInfo setPeekCount(int peekCount) {
      this.peekCount = peekCount;
      return this;
   }

}
