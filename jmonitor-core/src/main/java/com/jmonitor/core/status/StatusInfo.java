package com.jmonitor.core.status;

import com.jmonitor.core.status.model.DiskInfo;
import com.jmonitor.core.status.model.MemoryInfo;
import com.jmonitor.core.status.model.OsInfo;
import com.jmonitor.core.status.model.ThreadsInfo;
import com.jmonitor.core.util.JsonUtil;

import java.util.Date;

public class StatusInfo{
   private Date timestamp;
   private OsInfo os;
   private DiskInfo disk;
   private MemoryInfo memory;
   private ThreadsInfo thread;

   public StatusInfo() {
       this.timestamp = new Date();
       this.os = new OsInfo();
       this.disk = new DiskInfo();
       this.memory = new MemoryInfo();
       this.thread = new ThreadsInfo();
   }

   public DiskInfo getDisk() {
      return disk;
   }

   public MemoryInfo getMemory() {
      return memory;
   }

   public OsInfo getOs() {
      return os;
   }

   public ThreadsInfo getThread() {
      return thread;
   }

   public Date getTimestamp() {
      return timestamp;
   }

   public StatusInfo setDisk(DiskInfo disk) {
      this.disk = disk;
      return this;
   }

   public StatusInfo setMemory(MemoryInfo memory) {
      this.memory = memory;
      return this;
   }

   public StatusInfo setOs(OsInfo os) {
      this.os = os;
      return this;
   }

   public StatusInfo setThread(ThreadsInfo thread) {
      this.thread = thread;
      return this;
   }

   public StatusInfo setTimestamp(Date timestamp) {
      this.timestamp = timestamp;
      return this;
   }

    @Override
    public String toString() {
        return JsonUtil.toJson(this);
    }

}
