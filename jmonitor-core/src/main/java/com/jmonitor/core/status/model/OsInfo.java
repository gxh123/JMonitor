package com.jmonitor.core.status.model;

public class OsInfo {
   private String name;
   private String arch;
   private int availableProcessors;
   private long totalPhysicalMemory;
   private double processorUseRatio;

   public OsInfo() {

   }

   public double getProcessorUseRatio() {
      return processorUseRatio;
   }

   public void setProcessorUseRatio(double processorUseRatio) {
      this.processorUseRatio = processorUseRatio;
   }

   public String getArch() {
      return arch;
   }

   public int getAvailableProcessors() {
      return availableProcessors;
   }

   public String getName() {
      return name;
   }

   public long getTotalPhysicalMemory() {
      return totalPhysicalMemory;
   }

   public OsInfo setArch(String arch) {
      this.arch = arch;
      return this;
   }

   public OsInfo setAvailableProcessors(int availableProcessors) {
      this.availableProcessors = availableProcessors;
      return this;
   }

   public OsInfo setName(String name) {
      this.name = name;
      return this;
   }

   public OsInfo setTotalPhysicalMemory(long totalPhysicalMemory) {
      this.totalPhysicalMemory = totalPhysicalMemory;
      return this;
   }

}
