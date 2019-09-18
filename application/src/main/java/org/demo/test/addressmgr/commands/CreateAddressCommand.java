package org.demo.test.addressmgr.commands;

import com.sap.cloud.sdk.s4hana.connectivity.ErpHttpDestination;
import com.sap.cloud.sdk.s4hana.datamodel.odata.namespaces.businesspartner.BusinessPartnerAddress;
import com.sap.cloud.sdk.s4hana.datamodel.odata.services.BusinessPartnerService;

public class CreateAddressCommand {

	private final BusinessPartnerService service;
    private final BusinessPartnerAddress addressToCreate;
    
    private final ErpHttpDestination destination;
    
    
    public CreateAddressCommand(ErpHttpDestination destination, final BusinessPartnerService service, final BusinessPartnerAddress addressToCreate) {
    	this.destination = destination;
        this.service = service;
        this.addressToCreate = addressToCreate;
    }
    
    public BusinessPartnerAddress execute() throws Exception {
        return service
                .createBusinessPartnerAddress(addressToCreate)
                .execute(destination);
    }
    
}
