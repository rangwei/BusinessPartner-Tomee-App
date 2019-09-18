package org.demo.test.addressmgr;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.demo.test.addressmgr.commands.GetAllBusinessPartnersCommand;
import org.demo.test.addressmgr.commands.GetSingleBusinessPartnerByIdCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.sap.cloud.sdk.cloudplatform.connectivity.DestinationAccessor;
import com.sap.cloud.sdk.s4hana.connectivity.DefaultErpHttpDestination;
import com.sap.cloud.sdk.s4hana.connectivity.ErpHttpDestination;
import com.sap.cloud.sdk.s4hana.datamodel.odata.namespaces.businesspartner.BusinessPartner;
import com.sap.cloud.sdk.s4hana.datamodel.odata.services.BusinessPartnerService;
import com.sap.cloud.sdk.s4hana.datamodel.odata.services.DefaultBusinessPartnerService;

@WebServlet("/api/business-partners")
public class BusinessPartnerServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(BusinessPartnerServlet.class);
    
    private final BusinessPartnerService service = new DefaultBusinessPartnerService();

    private static final String DESTINATION_NAME = "MyErpSystem";

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
    	
    	final ErpHttpDestination destination = DestinationAccessor.getDestination(DESTINATION_NAME)
                .asHttp().decorate(DefaultErpHttpDestination::new);
    	
    	
    	final String id = request.getParameter("id");
    	
    	final String jsonResult;
    	
        try {
        	
        	if (id == null) {
        		logger.info("Retrieving all business partners");
        		
        		
                final List<BusinessPartner> businessPartners =
                        new GetAllBusinessPartnersCommand(destination, service).execute();
                
                jsonResult = new Gson().toJson(businessPartners);
        		
        	} else {
        		if (!validateInput(id)) {
                    logger.warn("Invalid request to retrieve a business partner, id: {}.", id);
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                            String.format("Invalid business partner ID '%s'. " +
                                            "Business partner ID must not be empty or longer than 10 characters.",
                                    id));
                    return;
                }
                logger.info("Retrieving business partner with id {}", id);
                
                final BusinessPartner result = new GetSingleBusinessPartnerByIdCommand(destination, service, id).execute();
                
                jsonResult = new Gson().toJson(result);
        	
        	}
        	
            response.setContentType("application/json");
            response.getWriter().write(jsonResult);
            
        } catch (final Exception e) {
            logger.error(e.getMessage(), e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(e.getMessage());
            e.printStackTrace(response.getWriter());
        }
    }
    
    private boolean validateInput(String id) {
        return !Strings.isNullOrEmpty(id) && id.length() <= 10;
    }
}
