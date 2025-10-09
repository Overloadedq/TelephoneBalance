package org.billing.domain;

import java.time.LocalDateTime;

public class UsageRecord {
    private final UsageType type;
    private final String source;
    private final String destination;
    private final LocalDateTime start;
    private final LocalDateTime end;
    private final long bytes;
    public UsageRecord(UsageType type, String source, String destination, LocalDateTime start, LocalDateTime end, long bytes) {

        this.type = type;

        this.source = source;
        this.destination = destination;
        this.start = start;
        this.end = end;


        this.bytes = bytes;

    }
    public UsageType getType() {
        return type;
    }
    public String getSource() {
        return source;
    }
    public String getDestination() {
        return destination;
    }
    public LocalDateTime getStart() {
        return start;
    }
    public LocalDateTime getEnd() {
        return end;
    }
    public long getBytes() {
        return bytes;
    }
}
