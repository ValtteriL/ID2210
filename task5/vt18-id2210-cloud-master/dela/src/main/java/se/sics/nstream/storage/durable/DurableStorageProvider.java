/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.sics.nstream.storage.durable;

import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.Init;
import se.sics.nstream.storage.durable.util.DStreamEndpoint;
import se.sics.nstream.storage.durable.util.DStreamResource;

/**
 * @author Alex Ormenisan <aaor@kth.se>
 */
public interface DurableStorageProvider<D extends ComponentDefinition> {
    public String getName();
    public DStreamEndpoint getEndpoint();
    public Class<D> getStorageDefinition();
    public <I extends Init<D>> I initiate(DStreamResource resource);
}
