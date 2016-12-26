package com.jmonitor.core.status.model;

public class MemoryInfo {
   private long max;
   private long total;
   private long heapUsage;

   public MemoryInfo() {
   }

   public long getHeapUsage() {
      return heapUsage;
   }

   public long getMax() {
      return max;
   }

   public long getTotal() {
      return total;
   }

   public MemoryInfo setHeapUsage(long heapUsage) {
      this.heapUsage = heapUsage;
      return this;
   }

   public MemoryInfo setMax(long max) {
      this.max = max;
      return this;
   }

   public MemoryInfo setTotal(long total) {
      this.total = total;
      return this;
   }

}
