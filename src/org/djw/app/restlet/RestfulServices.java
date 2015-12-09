package org.djw.app.restlet;

import org.djw.app.restletresources.*;
import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;
/**
 * 
 * 
 *
 * Maps all service urls to classes which handle the services. 
 * lookup - pulls data from the lookup tables used for autocomplete.
 * search - performs a search of the open.fda.gov service with given parameters.
 * druginfo - returns additional information on drugs from DailyMed RESTful service.
 * chart - returns data used to chart adverse reactions.
 */
public class RestfulServices extends Application {
	@Override
	public synchronized Restlet createInboundRoot() {
		Router router = new Router(getContext());
		router.attach("/test", TestResource.class);  //Test the resource. Returns success json
		router.attach("/test/{Error}", TestResource.class);  //Test the resource. Returns error json
		router.attach("/djwTools", InfoResource.class);		//returns version information
		router.attach("/fda/{ServerKey}/lookup/{lookuptype}", LookupResource.class);
		router.attach("/fda/{ServerKey}/lookup/{lookuptype}/{partial}", LookupResource.class);
		router.attach("/fda/{ServerKey}/search/reaction/{reactionlist}", OpenFDAResource.class);
		router.attach("/fda/{ServerKey}/search/drug/{druglist}", FDADrugResource.class);
		router.attach("/fda/{ServerKey}/chart/{ResultType}/{ThingList}", ChartResource.class);
		router.attach("/fda/{ServerKey}/druginfo/{DrugName}", DrugInfoResource.class);
		return router;
	}

}
