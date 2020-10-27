package br.ucs.trabalho.config;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class Configuracao {
	
	
	public Twitter ObterConfiguracao() {
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
		  .setOAuthConsumerKey("0AhYPKEblgpFYZ0VNCutciw3s")
		  .setOAuthConsumerSecret("hpnrlyPSoYJiQJM6JL2i8Y99j4dXoYQEnCEy1EDyOsxAeHBroX")
		  .setOAuthAccessToken("1082398505770119168-tPVDuT0RFKs7vpSFjaqYVRU9MzmcPK")
		  .setOAuthAccessTokenSecret("MbPt5iTZyRFYyhDbSLPgLLG9DNjpNVP9ZeMvgj8frXGuz");
		TwitterFactory tf = new TwitterFactory(cb.build());
		Twitter twitter = tf.getInstance();
		
		return twitter;
	}
}
