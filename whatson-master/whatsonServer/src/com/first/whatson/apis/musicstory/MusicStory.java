package com.first.whatson.apis.musicstory;


/**
 * Dialog with the MusicStory API
 * @author alice
 *
 */
public class MusicStory {

	private final String CONSUMERKEY = "19cc3dfef7886ba99d1b9d7f77c2de0174194254";
	private final String CONSUMERSECRET = "3911fc959dcbdd8d05dbc3c0ddfaded76d0a4d80";
	private final String ACCESSTOKEN = "d648da32f271a986283eddc07cbae3b0e6917d46";
	private final String TOKENSECRET =	"0cb3b8746bbe0b985326abf8f3f5630e04ef6d01";
	
	/**
	 * Obtain the signature for a query to Music-story API
	 */
//	public void getRequestSignature(String request){
//		String http_method = "GET";
//		String[] a = explode('?', request);
//		String host_uri = a[0];
//		String params;
//		if(a.length>=2)
//			params = a[1];
//		else params = null;
//		params = explode('&', params);
//		
//		if(isset(params['oauth_signature'])) unset($params['oauth_signature']);
//		sort($params);
//		ksort($params);
//		$encoded_parameters = implode('&',$params);
//
//		$base = str_replace('+', ' ', str_replace('%7E', '~', rawurlencode($http_method)));
//		$base.= '&';
//		$base.= str_replace('+', ' ', str_replace('%7E', '~', rawurlencode($host_uri)));
//		$base.= '&';
//		$base.= str_replace('+', ' ', str_replace('%7E', '~', rawurlencode($encoded_parameters)));
//
//		$hmac_key = str_replace('+', ' ', str_replace('%7E', '~', rawurlencode(CONSUMERSECRET)));
//		$hmac_key.= '&';
//		$hmac_key.= str_replace('+', ' ', str_replace('%7E', '~', rawurlencode(TOKENSECRET)));
//
//		$oauth_signature = base64_encode(hash_hmac('sha1', $base, $hmac_key, true));
//
//		return $request.(strpos($request, '?')?'&':'?').'oauth_signature='.rawurlencode($oauth_signature);		
//	}

}

