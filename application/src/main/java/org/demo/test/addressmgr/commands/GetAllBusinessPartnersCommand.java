package org.demo.test.addressmgr.commands;

import java.util.List;

import org.slf4j.Logger;

import com.sap.cloud.sdk.cloudplatform.logging.CloudLoggerFactory;
import com.sap.cloud.sdk.datamodel.odata.helper.Order;
import com.sap.cloud.sdk.s4hana.connectivity.ErpHttpDestination;
import com.sap.cloud.sdk.s4hana.datamodel.odata.namespaces.businesspartner.BusinessPartner;
import com.sap.cloud.sdk.s4hana.datamodel.odata.services.BusinessPartnerService;
import com.sap.cloud.sdk.s4hana.datamodel.odata.services.DefaultBusinessPartnerService;

public class GetAllBusinessPartnersCommand {
    private static final Logger logger = CloudLoggerFactory.getLogger(GetAllBusinessPartnersCommand.class);

    private static final String CATEGORY_PERSON = "1";
    
    private final ErpHttpDestination destination;

    private final BusinessPartnerService service;
    
    public GetAllBusinessPartnersCommand(ErpHttpDestination destination) {
    	this(destination, new DefaultBusinessPartnerService());
    }

    public GetAllBusinessPartnersCommand(ErpHttpDestination destination, final BusinessPartnerService service) {
    	this.destination = destination;
    	
        this.service = service;
    }

    public List<BusinessPartner> execute() throws Exception {
        return service
                .getAllBusinessPartner()
                .select(BusinessPartner.FIRST_NAME, BusinessPartner.LAST_NAME, BusinessPartner.BUSINESS_PARTNER)
                .filter(BusinessPartner.BUSINESS_PARTNER_CATEGORY.eq(CATEGORY_PERSON))
                .orderBy(BusinessPartner.LAST_NAME, Order.ASC)
                .execute(destination);
    }

}