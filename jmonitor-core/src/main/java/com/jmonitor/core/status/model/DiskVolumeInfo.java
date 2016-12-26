package com.jmonitor.core.status.model;

public class DiskVolumeInfo {
   private String id;
   private long total;
   private long usable;

   public DiskVolumeInfo(String id) {
      this.id = id;
   }

   public String getId() {
      return id;
   }

   public long getTotal() {
      return total;
   }

   public long getUsable() {
      return usable;
   }

   public DiskVolumeInfo setId(String id) {
      this.id = id;
      return this;
   }

   public DiskVolumeInfo setTotal(long total) {
      this.total = total;
      return this;
   }

   public DiskVolumeInfo setUsable(long usable) {
      this.usable = usable;
      return this;
   }

}
