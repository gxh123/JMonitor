package com.jmonitor.core.status.model;

import java.util.ArrayList;
import java.util.List;

public class DiskInfo{
   private List<DiskVolumeInfo> diskVolumes = new ArrayList<DiskVolumeInfo>();

   public DiskInfo() {

   }

   public DiskInfo addDiskVolume(DiskVolumeInfo diskVolume) {
      this.diskVolumes.add(diskVolume);
      return this;
   }

   public List<DiskVolumeInfo> getDiskVolumes() {
      return diskVolumes;
   }

   public long getTotal(){
       long total = 0;
       for(DiskVolumeInfo disk: diskVolumes){
           total += disk.getTotal();
       }
       return total;
   }
}
