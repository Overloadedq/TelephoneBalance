package org.billing.domain;

import java.time.LocalDateTime;

public class UsageRecordDB {
    private int id;
    private int subscriberId;
    private UsageType usageType;
    private LocalDateTime start;
    private int duration; // или количество данных, если интернет

    // Геттеры
    public int getId() { return id; }
    public int getSubscriberId() { return subscriberId; }
    public UsageType getUsageType() { return usageType; }
    public LocalDateTime getStart() { return start; }
    public int getDuration() { return duration; }

    // Сеттеры
    public void setId(int id) { this.id = id; }
    public void setSubscriberId(int subscriberId) { this.subscriberId = subscriberId; }
    public void setUsageType(UsageType usageType) { this.usageType = usageType; }
    public void setStart(LocalDateTime start) { this.start = start; }
    public void setDuration(int duration) { this.duration = duration; }
}
