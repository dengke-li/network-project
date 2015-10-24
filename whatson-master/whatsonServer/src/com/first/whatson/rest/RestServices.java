package com.first.whatson.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.first.whatson.algorithms.ImagesSelection;
import com.first.whatson.algorithms.Matchinfo;
import com.first.whatson.apis.echonest.ApiKey;
import com.first.whatson.apis.echonest.Matching;
import com.first.whatson.apis.echonest.TreeModelPlaylist;
import com.first.whatson.connection.User;
import com.first.whatson.database.objects.EmotionSpider;
import com.first.whatson.database.objects.PhotoTags;
import com.first.whatson.database.objects.TextExternalContent;
import com.first.whatson.database.tables.TableEmotions;
import com.first.whatson.database.tables.TableExterneTags;
import com.first.whatson.database.tables.TablePhotosTags;
import com.first.whatson.database.tables.TableUsers;
import com.first.whatson.servlet.LoginServlet;

/**
 * List of all the rest services that can be called by a remote client.<br>
 * <ul>
 * <li>Get the next images</li>
 * <li>Get user current emotional profile during the current session</li>
 * <li>Get the spotify GenreMatchingMusicPlaylist</li>
 * <li>Get the spotify embedded playlist that matche the current user profile</li>
 * <li>Get the list of sorted matching content between current user profile and externe sources</li>
 * </ul>
 * 
 *
 */
@Path("WhatsonServicesProvider")
public class RestServices {

	private static int iterations = 3;
	private static final String ITERATIONLABEL = "iteration";

	/**
	 * Get next images to display in JSON format
	 * @throws Exception 
	 */
	@GET
	@Path("/next/json/{userToken}/{lastId}/{otherId}")
	@Produces(MediaType.APPLICATION_JSON)
	public String nextImagesJson(@PathParam("userToken") String userToken,@PathParam("lastId") int lastId,@PathParam("otherId") int otherId,@Context HttpServletRequest request) throws Exception{
		//check if the session is active (user is connected) to get the user session bean
		System.out.println("Getting next images on whatsonserver, number of current connected users: "+ LoginServlet.connectedUsers.size());
		if(LoginServlet.isUserConnected(userToken)){
			String json = loadNextJson(lastId,otherId,LoginServlet.connectedUsers.get(userToken));
			if(json != null)
				return json.toString();
			return null;
		}
		System.err.println("Not connected! (trying to get next images)");
		return null;
	}	

	private String loadNextJson(int lastId,int otherId,User user) throws Exception {
		//if it is not the first (start) iteration, we add lastId in the list of previously selected ids
		if(lastId > 0){
			LinkedList<Integer> selectedPhotoIds = user.getSelectedPhotoIds();
			selectedPhotoIds.add(lastId);
			user.setSelectedPhotoIds(selectedPhotoIds);
			//if we limit the experience to a number of iteration, return null when we have reached the last iteration
			if(selectedPhotoIds.size() >= iterations){
				JSONObject jobj = new JSONObject();
				jobj.append(ITERATIONLABEL, selectedPhotoIds.size());
				return jobj.toString();
			}
			System.out.println(selectedPhotoIds);			
		}
		else
			user.setSelectedPhotoIds(new LinkedList<Integer>());
		ArrayList<Map<String,String>> nextImages = ImagesSelection.ImagesSelection(lastId,otherId);
		return listMapToJSON(nextImages,user.getSelectedPhotoIds().size());
	}
	
	public String listMapToJSON(List<Map<String, String>> list,int iteration) throws JSONException
	{   
		if(list!=null){
			JSONObject jobj = new JSONObject();
		    JSONArray array = new JSONArray();
		    
		    for (Map<String, String> map : list) {
		    	Set<String> keys = map.keySet();
				Iterator<String> it = keys.iterator();
				while (it.hasNext()){
					String key = it.next();
			        JSONObject object=new JSONObject();
			        object.put("id", key);
			        object.put("source", map.get(key));
			        array.put(object);
				}
		    }
		    jobj.append("nextImages", array);
		    jobj.append(ITERATIONLABEL, iteration);
		    return jobj.toString();
		}
		return null;
	}
	
	
	/**
	 * Get current data about this user session (current emotionnal state, selected photos)
	 * @throws Exception 
	 */
	@GET
	@Path("/currentUserData/{userToken}")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONArray currentUserData(@PathParam("userToken") String userToken) throws Exception{
		if(!LoginServlet.isUserConnected(userToken))
			return null;
		
		User user = LoginServlet.connectedUsers.get(userToken);
		
		//make emotion spider for the user
		setCurrentUserEmotionSpider(user);

		//return the json map of thecurrent emotion for the pie/chart/radiar presentation
		return mapToJSONArray(user.getCurrentEmotionsIntensities(),user);
	}

	/**
	 * Set the emotionSpider of the user for the current session
	 * @param user
	 * @throws Exception 
	 */
	private void setCurrentUserEmotionSpider(User user) throws Exception {
		LinkedList<Integer> selectedPhotoIds = user.getSelectedPhotoIds();
		//get each image's spider to get its phototag values
		LinkedList<PhotoTags> selectedPhotoWithTheirTags = new LinkedList<PhotoTags>();
		for(Integer key : selectedPhotoIds){
			selectedPhotoWithTheirTags.add(TablePhotosTags.getAllPhotoSpiders().get(key));
			//System.out.println("spider content "+TablePhotosTags.getAllPhotoSpiders().get(key));
		}

		//System.out.println("user selected photos ids : "+user.getSelectedPhotoIds().get(0));
		//System.out.println("with tag "+selectedPhotoWithTheirTags.get(0));
		
		Map<Integer,Double> currentEmotionsIntensity = new HashMap<Integer, Double>();
		
		//for each of emotion, make the average of the photos in the currentEmotions
		Set<Integer> keys = selectedPhotoWithTheirTags.get(0).getEmotions().keySet();
		Iterator<Integer> it = keys.iterator();
		int length = selectedPhotoWithTheirTags.size();
		while (it.hasNext()){
			Integer key = it.next();
			Double averageIntensityOfThisEmotion = 0.0;
			for(PhotoTags selectedPhoto : selectedPhotoWithTheirTags){
				averageIntensityOfThisEmotion += selectedPhoto.getEmotions().get(key);
				//System.out.println("average etape : "+averageIntensityOfThisEmotion);
			}
			averageIntensityOfThisEmotion /= length;
			//System.out.println("final average for the current emotion : "+averageIntensityOfThisEmotion);
			currentEmotionsIntensity.put(key, averageIntensityOfThisEmotion);
		}
		Map<Integer, Double> sortedCurrentEmotionsIntensity = ImagesSelection.sortByComparator(currentEmotionsIntensity);
		user.setCurrentEmotionsIntensities(sortedCurrentEmotionsIntensity);
	}

	private JSONArray mapToJSONArray(Map<Integer, Double> currentEmotionsIntensities, User user) throws Exception {
		if(currentEmotionsIntensities!=null){
		    JSONArray array = new JSONArray();
		    JSONArray object = new JSONArray();
		    object.put("Emotion");
		    object.put("Intensité ressentie");
		    //array.put(object);
		    String emotion;
		    Set<Integer> keys = currentEmotionsIntensities.keySet();
			Iterator<Integer> it = keys.iterator();
			int length = keys.size();
			int i=0;
			
			List<String> biggestEmotions = new LinkedList<String>();
			while (it.hasNext()){
				Integer key = it.next();
		        object=new JSONArray();
		        emotion = TableEmotions.getEmotionWithId(key);
		        if(emotion != null){
		        	object.put(emotion);
		        	object.put(currentEmotionsIntensities.get(key));
		        	// System.out.println("Json "+key+" "+ currentEmotionsIntensities.get(key));
		        	array.put(object);
		        }
		        
		        //get the main emotions
				if(i==(length-3) || i==(length-2)|| i==(length-1))
					biggestEmotions.add(emotion);
		        i++;
			}
			//set the most important emotion to the user current bean session
			user.setBiggestEmotions(biggestEmotions);
		    return array;
		}
		return null;
	}
	
	/**
	 * Get the music playlist that matches one of the biggest emotion
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@GET
	@Path("/MusicMatchingPlaylist")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONArray getGenreMatchingMusicPlaylist(@Context HttpServletRequest request) throws Exception{
		HttpSession session= request.getSession();
		User user = (User) session.getAttribute(LoginServlet.ATT_SESSION_USER);
		if(user==null)
			return null;
		
		
		Matching matching = new Matching();
		//get the current biggest emotion in order to do the match with the musical genre
		List<String> biggestEmotions = user.getBiggestEmotions();
		
		JSONArray array = new JSONArray();
		String emotion,musicGenre = null,getSpotifyIdTracks = null;
		boolean playlist = false;
		for(int i=biggestEmotions.size()-1;i>=0;i--){
			emotion = biggestEmotions.get(i);
			array.put(emotion);
			//if there is a match: define spotify embedded url for the playlist related to genre
			if(!playlist){
				if(matching.getMatchingFeelingGenre().containsKey(emotion)){
					musicGenre = matching.getMatchingFeelingGenre().get(emotion);
					playlist=true;
				}
			}
		}
		
		//will display the main emotions
		JSONObject jobj = new JSONObject();
		jobj.put("biggestEmotions", array);
		
		//will display the spotify url to get the matching playlist thanks to the predetermined musicGenre
		JSONObject jobj2 = new JSONObject();
		
		//get spotify idTracks for the playlist if we can make one (if there has been a match)
		if(playlist){
								//http://developer.echonest.com/api/v4/playlist/static?api_key=NG6LOLINXBHFJY9J6&genre=acid+techno&format=json&bucket=id%3Aspotify-WW&bucket=tracks&limit=true&variety=0.2&dmca=true&results=12&type=genre-radio

			getSpotifyIdTracks = "http://developer.echonest.com/api/v4/playlist/static?api_key="+ApiKey.APIKEY+"&genre="+musicGenre+"&format=json&bucket=id%3Aspotify-WW&bucket=tracks&limit=true&variety=0.2&dmca=true&results=10&type=genre-radio";
			TreeModelPlaylist tmp = new TreeModelPlaylist(getSpotifyIdTracks);
			jobj2.put("spotifyPlaylist", makeSpotifyEmbeddedPlaylist(musicGenre, tmp.getIdTracks()));
		}

		JSONArray jobj3 = new JSONArray();
		jobj3.put(jobj);
		jobj3.put(jobj2);
		return jobj3;
	}

	private String makeSpotifyEmbeddedPlaylist(String genre, List<String> idTracks) {
		if(idTracks!=null && idTracks.size()>0 && genre != null){
			String url = "https://embed.spotify.com/?uri=spotify:trackset:";
			url += genre;
			url += "%20playlist:";
			for(String idTrack : idTracks){
				//System.out.println(idTrack);
				url += idTrack+",";
			}
			return url.replaceAll(" ", "%20");
		}
		return null;
	}
	
	
	/**
	 * Get contents that match with user emotionnal profile
	 * @throws Exception 
	 */
	@GET
	@Path("/matchingContent/{userToken}/{contentType}")
	@Produces(MediaType.APPLICATION_JSON)
	public String matchingContentList(@PathParam("userToken") String userToken,@PathParam("contentType") int contentType) throws Exception{
		//check if the session is active (user is connected) to get the user session bean
		System.out.println("Getting matching contents on whatsonserver");
		if(LoginServlet.isUserConnected(userToken)){
			//get user spider			
			User user = LoginServlet.connectedUsers.get(userToken);
			setCurrentUserEmotionSpider(user);

			//TODO save this new user emotion spider in the database
			
			//sort external contents spiders to have the matching list for results with the current user spider
			 LinkedList<TextExternalContent> sortedEmotionSpiderList= Matchinfo.match(user.getCurrentEmotionsIntensities(), TableUsers.getUserHistoricEmotions(), TableExterneTags.getExternalContents(contentType));

			//store this resulted list in user bean for the current user session
			user.setSortedEmotionSpiderList(sortedEmotionSpiderList);
			
			//transform the linked list of sorted results into a json response
			JSONArray array = new JSONArray();
			for(EmotionSpider spider : sortedEmotionSpiderList){
				//according to media type of external content, create json object for the response
				JSONObject jobj;

				//if text source content
				if(spider instanceof TextExternalContent){
					TextExternalContent textContent = (TextExternalContent) spider;
					jobj = new JSONObject();
					jobj.put("type", "text");
					jobj.put("id", textContent.getId());
					jobj.put("title", textContent.getTitle());
					jobj.put("source", textContent.getSource());
					jobj.put("url", textContent.getUrl());
					jobj.put("author", textContent.getAuthor());
					array.put(jobj);
				}
				
			}
			System.out.println(array.toString());
			return array.toString();

			
			//fixed result for tests
			
//			JSONArray array = new JSONArray();
//			JSONObject jobj;
//			for(int i = 0;i<4;i++){
//				jobj = new JSONObject();
//				jobj.put("type", "text");
//				jobj.put("id", i+"");
//				jobj.put("title", "Article du monde numero "+i);
//				jobj.put("source", "Le Monde");
//				jobj.put("url", "http://mobile.lemonde.fr/europeennes-2014/article/2014/05/25/les-francais-ont-vote-en-majorite-en-fonction-d-enjeux-europeens_4425697_4350146.html");
//				jobj.put("author", "John Doe");
//				array.put(jobj);
//			}
//			
//			return array.toString();
		}
		System.err.println("Not connected! (trying to get matching mood contents)");
		return null;
	}	
	
}
