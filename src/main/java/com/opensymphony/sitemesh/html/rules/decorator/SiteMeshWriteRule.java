package com.opensymphony.sitemesh.html.rules.decorator;

import com.opensymphony.sitemesh.tagprocessor.BasicBlockRule;
import com.opensymphony.sitemesh.tagprocessor.Tag;
import com.opensymphony.sitemesh.Content;
import com.opensymphony.sitemesh.SiteMeshContext;

import java.io.IOException;

/**
 * Replaces tags that look like {@code <sitemesh:write property='foo'/>} with the property of the
 * {@link Content} being merged into the current document. The body contents of the tag will be
 * discarded.
 *
 * @author Joe Walnes
 * @see SiteMeshContext#getContentToMerge()
 */
public class SiteMeshWriteRule extends BasicBlockRule {

    private final SiteMeshContext siteMeshContext;

    public SiteMeshWriteRule(SiteMeshContext siteMeshContext) {
        this.siteMeshContext = siteMeshContext;
    }

    @Override
    protected Object processStart(Tag tag) throws IOException {
        String propertyName = tag.getAttributeValue("property", true);
        Content contentToMerge = siteMeshContext.getContentToMerge();
        if (contentToMerge != null) {
            Content.Property property = contentToMerge.getProperty(propertyName);
            if (property.exists()) {
                property.writeTo(tagProcessorContext.currentBuffer());
            }
        }
        tagProcessorContext.pushBuffer();
        return null;
    }

    @Override
    protected void processEnd(Tag tag, Object data) throws IOException {
        tagProcessorContext.popBuffer();
    }
}
