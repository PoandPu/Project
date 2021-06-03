package ua.epam.pavelchuk.final_project.web.captcha;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.net.ssl.HttpsURLConnection;

import org.apache.log4j.Logger;

public class VerifyUtils {
	
	private VerifyUtils() {
	}
	
	private static final Logger LOG = Logger.getLogger(VerifyUtils.class);
	public static final String SITE_KEY ="6LfjBPgaAAAAAGIBKp2EaiqRU6aaH_kpGAhMBwIb";
	    
	public static final String SECRET_KEY ="6LfjBPgaAAAAAI3fGfL9wrpZyGW6n45tazIiv8Ij";

	public static final String SITE_VERIFY_URL = //
			"https://www.google.com/recaptcha/api/siteverify";

	public static boolean verify(String gRecaptchaResponse) {
		if (gRecaptchaResponse == null || gRecaptchaResponse.length() == 0) {
			return false;
		}
		
		JsonReader jsonReader = null;

		try {
			URL verifyUrl = new URL(SITE_VERIFY_URL);

			// Open a Connection to the URL above.
			HttpsURLConnection conn = (HttpsURLConnection) verifyUrl.openConnection();

			// Add Header information to Request to prepare a dispatch to server.
			conn.setRequestMethod("POST");
			conn.setRequestProperty("User-Agent", "Mozilla/5.0");
			conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

			// The data will be sent to Server.
			String postParams = "secret=" + SECRET_KEY //
					+ "&response=" + gRecaptchaResponse;

			// Send Request
			conn.setDoOutput(true);

			// Get the Output Stream of the connection to the Server.
			// Writing data to Output Stream means sending information to Server.
			OutputStream outStream = conn.getOutputStream();
			outStream.write(postParams.getBytes());

			outStream.flush();
			outStream.close();

			// The response code is returned from Server.
			int responseCode = conn.getResponseCode();
			LOG.trace("responseCode=" + responseCode);

			// Get Input Stream Connection to read data sent from Server.
			InputStream is = conn.getInputStream();
			
			jsonReader = Json.createReader(is);
			JsonObject jsonObject = jsonReader.readObject();
			jsonReader.close();

			LOG.trace("Response: " + jsonObject);
			
			return jsonObject.getBoolean("success");
		} catch (Exception e) {
			LOG.error(e.getMessage());
			return false;
		} finally {
			if (jsonReader != null) {
				jsonReader.close();
			}
		}
	}
}