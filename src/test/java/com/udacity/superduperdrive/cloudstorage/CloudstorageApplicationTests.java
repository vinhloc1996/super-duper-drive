package com.udacity.superduperdrive.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.util.ObjectUtils;

import java.io.File;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private WebDriver driver;

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void beforeEach() {
		this.driver = new ChromeDriver();
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}

	@Test
	@Order(0)
	public void testSignUpAndLoginFlow() throws InterruptedException {

		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		driver.get("http://localhost:" + this.port + "/home");
		Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
		doMockSignUp("URL","Test","UT","123");
		doLogIn("UT", "123");
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("btnLogout")));
		WebElement buttonSignUp = driver.findElement(By.id("btnLogout"));
		buttonSignUp.click();
		webDriverWait.until(ExpectedConditions.titleContains("Login"));
		Assertions.assertEquals("Login", driver.getTitle());
		driver.get("http://localhost:" + this.port + "/home");
		Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
	}

	@Test
	@Order(1)
	public void getLoginPage() {
		driver.get("http://localhost:" + this.port + "/login");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/
	private void doMockSignUp(String firstName, String lastName, String userName, String password){
		// Create a dummy account for logging in later.

		// Visit the sign-up page.
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		driver.get("http://localhost:" + this.port + "/signup");
		webDriverWait.until(ExpectedConditions.titleContains("Sign Up"));

		// Fill out credentials
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputFirstName")));
		WebElement inputFirstName = driver.findElement(By.id("inputFirstName"));
		inputFirstName.click();
		inputFirstName.sendKeys(firstName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputLastName")));
		WebElement inputLastName = driver.findElement(By.id("inputLastName"));
		inputLastName.click();
		inputLastName.sendKeys(lastName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement inputUsername = driver.findElement(By.id("inputUsername"));
		inputUsername.click();
		inputUsername.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement inputPassword = driver.findElement(By.id("inputPassword"));
		inputPassword.click();
		inputPassword.sendKeys(password);

		// Attempt to sign up.
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonSignUp")));
		WebElement buttonSignUp = driver.findElement(By.id("buttonSignUp"));
		buttonSignUp.click();

		/* Check that the sign up was successful.
		// You may have to modify the element "success-msg" and the sign-up
		// success message below depening on the rest of your code.
		*/
//		Assertions.assertTrue(driver.findElement(By.id("success-msg")).getText().contains("You successfully signed up!"));
		Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
	}



	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/
	private void doLogIn(String userName, String password)
	{
		// Log in to our dummy account.
		driver.get("http://localhost:" + this.port + "/login");
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement loginUserName = driver.findElement(By.id("inputUsername"));
		loginUserName.click();
		loginUserName.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement loginPassword = driver.findElement(By.id("inputPassword"));
		loginPassword.click();
		loginPassword.sendKeys(password);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));
		WebElement loginButton = driver.findElement(By.id("login-button"));
		loginButton.click();

		webDriverWait.until(ExpectedConditions.titleContains("Home"));

	}

	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
	 * rest of your code.
	 * This test is provided by Udacity to perform some basic sanity testing of
	 * your code to ensure that it meets certain rubric criteria.
	 *
	 * If this test is failing, please ensure that you are handling redirecting users
	 * back to the login page after a succesful sign up.
	 * Read more about the requirement in the rubric:
	 * https://review.udacity.com/#!/rubrics/2724/view
	 */
	@Test
	@Order(2)
	public void testRedirection() {
		// Create a test account
		doMockSignUp("Redirection","Test","RT","123");

		// Check if we have been redirected to the log in page.
//		Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
	}

	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
	 * rest of your code.
	 * This test is provided by Udacity to perform some basic sanity testing of
	 * your code to ensure that it meets certain rubric criteria.
	 *
	 * If this test is failing, please ensure that you are handling bad URLs
	 * gracefully, for example with a custom error page.
	 *
	 * Read more about custom error pages at:
	 * https://attacomsian.com/blog/spring-boot-custom-error-page#displaying-custom-error-page
	 */
	@Test
	@Order(3)
	public void testBadUrl() {
		// Create a test account
		doMockSignUp("URL","Test","UT1","123");
		doLogIn("UT1", "123");

		// Try to access a random made-up URL.
		driver.get("http://localhost:" + this.port + "/some-random-page");
		Assertions.assertFalse(driver.getPageSource().contains("Whitelabel Error Page"));
	}


	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
	 * rest of your code.
	 * This test is provided by Udacity to perform some basic sanity testing of
	 * your code to ensure that it meets certain rubric criteria.
	 *
	 * If this test is failing, please ensure that you are handling uploading large files (>1MB),
	 * gracefully in your code.
	 *
	 * Read more about file size limits here:
	 * https://spring.io/guides/gs/uploading-files/ under the "Tuning File Upload Limits" section.
	 */
	@Test
	@Order(4)
	public void testLargeUpload() {
		// Create a test account
		doMockSignUp("Large File","Test","LFT","123");
		doLogIn("LFT", "123");

		// Try to upload an arbitrary large file
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		String fileName = "upload5m.zip";

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fileUpload")));
		WebElement fileSelectButton = driver.findElement(By.id("fileUpload"));
		fileSelectButton.sendKeys(new File(fileName).getAbsolutePath());

		WebElement uploadButton = driver.findElement(By.id("uploadButton"));
		uploadButton.click();
		try {
			webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("success")));
		} catch (org.openqa.selenium.TimeoutException e) {
			System.out.println("Large File upload failed");
		}
		Assertions.assertFalse(driver.getPageSource().contains("HTTP Status 403 – Forbidden"));

	}

	// test note
	@Test
	@Order(5)
	public void testNote() throws InterruptedException {
		doMockSignUp("My","Name","main","123");
		doLogIn("main", "123");
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
		driver.findElement(By.id("nav-notes-tab")).click();

		//add
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("new-note")));
		driver.findElement(By.id("new-note")).click();
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
		WebElement note_title = driver.findElement(By.id("note-title"));
		note_title.click();
		note_title.sendKeys("test1");
		WebElement note_desc = driver.findElement(By.id("note-description"));
		note_desc.click();
		note_desc.sendKeys("test desc1");

		webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("note-save")));
		WebElement note_save = driver.findElement(By.id("note-save"));
		note_save.click();
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
		driver.findElement(By.id("nav-notes-tab")).click();
		Thread.sleep(500);
		webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("note-success-msg")));
		Assertions.assertTrue(driver.findElement(By.id("note-success-msg")).getText().equals("Add note successfully."));
		//Switch to tab
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
		driver.findElement(By.id("nav-notes-tab")).click();
		Thread.sleep(500);
		var noteTable = driver.findElement(By.id("userTable"));
		var firstCred = noteTable.findElement(By.tagName("tr"));
		var title1 = firstCred.findElement(By.xpath("//table[@id='userTable']/tbody/tr/th"));
		Assertions.assertTrue(title1.getText().equals("test1"));
		var desc1 = firstCred.findElements(By.xpath("//table[@id='userTable']/tbody/tr/td")).get(1);
		Assertions.assertTrue(desc1.getText().contains("test desc1"));

		//edit
		WebElement editButton = driver.findElement(By.id("btnEditNote"));
		editButton.click();
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
		note_title = driver.findElement(By.id("note-title"));
		note_title.click();
		note_title.clear();
		note_title.sendKeys("new title");
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-description")));
		note_desc = driver.findElement(By.id("note-description"));
		note_desc.click();
		note_desc.clear();
		note_desc.sendKeys("new desc");
		driver.findElement(By.id("note-save")).click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
		driver.findElement(By.id("nav-notes-tab")).click();
		Thread.sleep(500);
		webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("note-success-msg")));
		Assertions.assertTrue(driver.findElement(By.id("note-success-msg")).getText().equals("Update note successfully."));
		noteTable = driver.findElement(By.id("userTable"));
		firstCred = noteTable.findElement(By.tagName("tr"));
		var title2 = firstCred.findElement(By.xpath("//table[@id='userTable']/tbody/tr/th"));
		Assertions.assertTrue(title2.getText().equals("new title"));
		var desc2 = firstCred.findElements(By.xpath("//table[@id='userTable']/tbody/tr/td")).get(1);
		Assertions.assertTrue(desc2.getText().contains("new desc"));

		//delete
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("userTable")));
		var deleteBtn = firstCred.findElement(By.xpath("//table[@id='userTable']/tbody/tr/td/a"));
		deleteBtn.click();
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
		driver.findElement(By.id("nav-notes-tab")).click();
		Thread.sleep(500);
		webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("note-success-msg")));
		Assertions.assertTrue(driver.findElement(By.id("note-success-msg")).getText().equals("Note successfully deleted."));

		noteTable = driver.findElement(By.id("userTable"));
		firstCred = noteTable.findElement(By.tagName("tr"));
		var checkCred = firstCred.findElements(By.xpath("//table[@id='userTable']/tbody/tr"));
		Assertions.assertTrue(checkCred.size() == 0);
	}

	// test credentials
	@Test
	@Order(6)
	public void testCredentials() throws InterruptedException {
//		doMockSignUp("My","Name","main","123");
		doLogIn("main", "123");
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
		driver.findElement(By.id("nav-credentials-tab")).click();

		//add
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("new-credential")));
		driver.findElement(By.id("new-credential")).click();
		WebElement credential_url = driver.findElement(By.id("credential-url"));
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
		credential_url.click();
		credential_url.sendKeys("http://localhost:" + this.port + "/username");
		WebElement credential_username = driver.findElement(By.id("credential-username"));
		credential_username.click();
		credential_username.sendKeys("username");
		WebElement credential_password = driver.findElement(By.id("credential-password"));
		credential_password.click();
		credential_password.sendKeys("123");

		webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("credential-save")));
		WebElement credential_save = driver.findElement(By.id("credential-save"));
		credential_save.click();
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
		driver.findElement(By.id("nav-credentials-tab")).click();
		Thread.sleep(1000);
		webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("credential-success-msg")));
		Assertions.assertTrue(driver.findElement(By.id("credential-success-msg")).getText().equals("Add credential successfully."));
		//Switch to tab
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
		driver.findElement(By.id("nav-credentials-tab")).click();
		Thread.sleep(500);
		var credentialTable = driver.findElement(By.id("credentialTable"));
		var firstCred = credentialTable.findElement(By.tagName("tr"));
		var url1 = firstCred.findElement(By.xpath("//table[@id='credentialTable']/tbody/tr/th"));
		Assertions.assertTrue(url1.getText().equals("http://localhost:" + this.port + "/username"));
		var usernameAndPassword = firstCred.findElements(By.xpath("//table[@id='credentialTable']/tbody/tr/td"));
		Assertions.assertTrue(usernameAndPassword.get(1).getText().contains("username"));
		var oldPass = usernameAndPassword.get(2).getText();
		Assertions.assertFalse(oldPass.isEmpty());

		//edit
		WebElement editButton = driver.findElement(By.id("btnEditCredential"));
		editButton.click();
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
		credential_url = driver.findElement(By.id("credential-url"));
		credential_url.click();
		credential_url.clear();
		credential_url.sendKeys("http://localhost:" + this.port + "/username222");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-username")));
		credential_username = driver.findElement(By.id("credential-username"));
		credential_username.click();
		credential_username.clear();
		credential_username.sendKeys("newusername");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-password")));
		credential_password = driver.findElement(By.id("credential-password"));
		credential_password.click();
		credential_password.clear();
		credential_password.sendKeys("222");
		driver.findElement(By.id("credential-save")).click();
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
		driver.findElement(By.id("nav-credentials-tab")).click();
		Thread.sleep(500);
		webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("credential-success-msg")));
		Assertions.assertTrue(driver.findElement(By.id("credential-success-msg")).getText().equals("Update credential successfully."));
		credentialTable = driver.findElement(By.id("credentialTable"));
		firstCred = credentialTable.findElement(By.tagName("tr"));
		var url2 = firstCred.findElement(By.xpath("//table[@id='credentialTable']/tbody/tr/th"));
		Assertions.assertTrue(url2.getText().equals("http://localhost:" + this.port + "/username222"));
		var usernameAndPassword1 = firstCred.findElements(By.xpath("//table[@id='credentialTable']/tbody/tr/td"));
		Assertions.assertTrue(usernameAndPassword1.get(1).getText().contains("newusername"));
		var newPass = usernameAndPassword1.get(2).getText();
		Assertions.assertFalse(newPass.equals(oldPass));

		//delete
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialTable")));
		var deleteBtn = firstCred.findElement(By.xpath("//table[@id='credentialTable']/tbody/tr/td/a"));
		deleteBtn.click();
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
		driver.findElement(By.id("nav-credentials-tab")).click();
		Thread.sleep(500);
		webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("credential-success-msg")));
		Assertions.assertTrue(driver.findElement(By.id("credential-success-msg")).getText().equals("Delete credential successfully."));

		credentialTable = driver.findElement(By.id("credentialTable"));
		firstCred = credentialTable.findElement(By.tagName("tr"));
		var checkCred = firstCred.findElements(By.xpath("//table[@id='credentialTable']/tbody/tr"));
		Assertions.assertTrue(checkCred.size() == 0);
	}

}
