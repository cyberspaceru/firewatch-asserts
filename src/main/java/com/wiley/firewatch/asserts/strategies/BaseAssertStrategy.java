package com.wiley.firewatch.asserts.strategies;

import com.wiley.firewatch.asserts.processing.ProcessingEntries;
import com.wiley.firewatch.asserts.processing.ProcessingMetadata;
import com.wiley.firewatch.core.FirewatchConnection;
import lombok.extern.slf4j.Slf4j;
import net.lightbody.bmp.core.har.HarNameValuePair;
import net.lightbody.bmp.core.har.HarPostDataParam;
import org.apache.commons.lang3.text.StrBuilder;
import org.testng.Assert;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
public class BaseAssertStrategy implements IAssertStrategy {

    private final String errorMessage;

    public BaseAssertStrategy(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public void execute(List<ProcessingEntries> processing) {
        for (ProcessingEntries entries : processing) {
            if (!entries.finished()) {
                StrBuilder details = new StrBuilder();
                try {
                    if (FirewatchConnection.proxyServer().getHar() != null) {
                        details.append("\n[DETAILS]");
                        details.append("\nHar allBestEntries: ").append(FirewatchConnection.proxyServer().getHar().getLog().getEntries().size());
                        details.append("\nHar allBestEntries hash code: ").append(FirewatchConnection.proxyServer().getHar().getLog().getEntries().hashCode());
                        Optional.ofNullable(entries.firewatchRequest()).ifPresent(x -> details.append("\n").append(x.toString()));
                        Optional.ofNullable(entries.firewatchResponse()).ifPresent(x -> details.append("\n").append(x.toString()));
                        Optional.ofNullable(entries.bestOverlap()).ifPresent(e -> {
                            Optional.ofNullable(e.request()).map(ProcessingMetadata::har).filter(Objects::nonNull).ifPresent(x -> {
                                details.append("\n[Request]\n").append("Overlap: ").append(e.request().overlap()).append("\n");
                                details.append("URL: ").append(x.getUrl()).append("\n");
                                details.append("Method: ").append(x.getMethod()).append("\n");
                                details.append("Params: ");
                                if (x.getPostData() != null && x.getPostData().getParams() != null) {
                                    for (HarPostDataParam param : x.getPostData().getParams()) {
                                        details.append("\n\t").append(param.getName()).append(": ").append(param.getValue());
                                    }
                                } else if (x.getQueryString() != null) {
                                    for (HarNameValuePair pair : x.getQueryString()) {
                                        details.append("\n\t").append(pair.getName()).append(": ").append(pair.getValue());
                                    }
                                }
                                details.append("\nHeaders: ");
                                for (HarNameValuePair pair : x.getHeaders()) {
                                    details.append("\n\t").append(pair.getName()).append(": ").append(pair.getValue());
                                }
                                Optional.ofNullable(x.getPostData()).ifPresent(harPostData ->
                                        details.append("\nPost Data: \n").append(x.getPostData().getText()).append("\n")
                                );
                            });
                            Optional.ofNullable(e.response()).map(ProcessingMetadata::har).filter(Objects::nonNull).ifPresent(x -> {
                                details.append("\n[Response]\n").append("Overlap: ").append(e.response().overlap()).append("\n");
                                details.append("Status: ").append(x.getStatus()).append("\n");
                                details.append("Headers: ");
                                for (HarNameValuePair pair : x.getHeaders()) {
                                    details.append("\n\t").append(pair.getName()).append(": ").append(pair.getValue());
                                }
                            });
                        });
                    } else {
                        details.append("Har is empty.");
                    }
                } catch (Exception ignore) {
                    details.append("\nERROR DURING PREPARATION DETAILS.");
                }
                Assert.fail(errorMessage + details.toString());
            }
        }
    }
}
