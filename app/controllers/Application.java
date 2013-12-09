package controllers;

import java.io.IOException;
import com.google.protobuf.InvalidProtocolBufferException;

import net.antidot.api.common.Service;
import net.antidot.api.search.CoderManager;
import net.antidot.api.search.Connector;
import net.antidot.api.search.Facet;
import net.antidot.api.search.FacetRegistry;
import net.antidot.api.search.FacetType;
import net.antidot.api.search.ProtobufExtensionManager;
import net.antidot.api.search.Query;
import net.antidot.api.search.QueryCoder;
import net.antidot.api.search.QueryManager;
import net.antidot.api.search.RepliesHelper;
import net.antidot.protobuf.reply.Reply.replies;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.meta;

public class Application extends Controller {
    public static Result index() throws IOException, InvalidProtocolBufferException {
		// Register facets
		FacetRegistry facetRegistry = new FacetRegistry();
		facetRegistry.addFacet(new Facet("Organization", FacetType.STRING));
		facetRegistry.addFacet(new Facet("date_parution", FacetType.DATE));
		facetRegistry.addFacet(new Facet("geo", FacetType.STRING));
		facetRegistry.addFacet(new Facet("media", FacetType.STRING));
		facetRegistry.addFacet(new Facet("person", FacetType.STRING));
		facetRegistry.addFacet(new Facet("period", FacetType.DATE));
		facetRegistry.addFacet(new Facet("source", FacetType.STRING));
		facetRegistry.addFacet(new Facet("taxo_iptc", FacetType.STRING));
		facetRegistry.addFacet(new Facet("theme", FacetType.STRING));
		facetRegistry.addFacet(new Facet("type", FacetType.STRING));
		facetRegistry.addFacet(new Facet("pays", FacetType.STRING));

		// Initialize connector and query manager
		Connector connector = new Connector("eval.partners.antidot.net", new Service(70000));
		QueryManager queryManager = new QueryManager(connector, facetRegistry);
		
		// Decode query from request parameters
		CoderManager coderMgr = new CoderManager();
		QueryCoder queryCoder = new QueryCoder(request().path(), coderMgr);
		Query query = queryCoder.decode(request().queryString());

		// Query AFS search engine
		byte[] reply = queryManager.send(query);

		replies result = replies.parseFrom(reply, ProtobufExtensionManager.getResgistry());

		RepliesHelper helper = new RepliesHelper(result, facetRegistry, queryCoder, query);
    	return ok(meta.render(helper));
    }

}
