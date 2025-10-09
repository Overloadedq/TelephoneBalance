package org.billing.io;

import org.billing.domain.UsageRecord;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface UsageReader {
    List<UsageRecord> read(Path path) throws IOException;
}
