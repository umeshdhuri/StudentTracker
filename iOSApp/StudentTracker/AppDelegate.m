//
//  AppDelegate.m
//  StudentTracker
//
//  Created by AppKnetics on 21/05/15.
//  Copyright (c) 2015 AppKnetics. All rights reserved.
//

#import "AppDelegate.h"
#import "TutirialViewController.h"
#import "TutorialScreenViewController.h"
#import "DrawerViewController.h"
#import "SignInScreenViewController.h"
#import "HomeScreenViewController.h"

@implementation AppDelegate
@synthesize deviceToken ;
- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
{
    self.window = [[UIWindow alloc] initWithFrame:[[UIScreen mainScreen] bounds]];
    [[UIApplication sharedApplication] setStatusBarHidden:NO withAnimation:UIStatusBarAnimationNone];
    // Override point for customization after application launch.
    self.window.backgroundColor = [UIColor whiteColor];
    [self.window makeKeyAndVisible];
    
#if __IPHONE_OS_VERSION_MAX_ALLOWED >= 80000
    // The following line must only run under iOS 8. This runtime check prevents
    // it from running if it doesn't exist (such as running under iOS 7 or earlier).
    if ([application respondsToSelector:@selector(registerUserNotificationSettings:)]) {
        [application registerUserNotificationSettings:[UIUserNotificationSettings settingsForTypes:UIUserNotificationTypeAlert|UIUserNotificationTypeBadge|UIUserNotificationTypeSound categories:nil]];
        
    }
#else
    [[UIApplication sharedApplication]
     registerForRemoteNotificationTypes:(UIRemoteNotificationTypeAlert | UIRemoteNotificationTypeBadge | UIRemoteNotificationTypeSound)];
    
#endif
    
    NSUserDefaults *userDefault = [NSUserDefaults standardUserDefaults];
    if([[userDefault valueForKey:@"dontshowagain"] isEqualToString:@"on"]) {
        
        if([userDefault valueForKey:@"student_id"] && ![[userDefault valueForKey:@"student_id"] isKindOfClass:[NSNull class]] && [[userDefault valueForKey:@"student_id"] length] > 0) {
        
            HomeScreenViewController *tutorialview=[[HomeScreenViewController alloc]init];
            sliderDrawe1ViewController *navigation=[[sliderDrawe1ViewController alloc]initWithRootViewController:tutorialview];
            
            [self.window setRootViewController:navigation];
        }else{
            SignInScreenViewController *tutorialview=[[SignInScreenViewController alloc]init];
            
            sliderDrawe1ViewController *navigation=[[sliderDrawe1ViewController alloc]initWithRootViewController:tutorialview];
            [self.window setRootViewController:navigation];
        }
        
        
        
    }else if([[userDefault valueForKey:@"dontshowagain"] isEqualToString:@"off"]) {
        TutorialScreenViewController *tutorialview=[[TutorialScreenViewController alloc]init];
        
        sliderDrawe1ViewController *navigation=[[sliderDrawe1ViewController alloc]initWithRootViewController:tutorialview];
        [self.window setRootViewController:navigation];
    }else{
        TutorialScreenViewController *tutorialview=[[TutorialScreenViewController alloc]init];
        
        sliderDrawe1ViewController *navigation=[[sliderDrawe1ViewController alloc]initWithRootViewController:tutorialview];
        [self.window setRootViewController:navigation];
    }
    
    
    
    return YES;
}

#if __IPHONE_OS_VERSION_MAX_ALLOWED >= 80000
- (void)application:(UIApplication *)application didRegisterUserNotificationSettings:(UIUserNotificationSettings *)notificationSettings
{
    //register to receive notifications
    [application registerForRemoteNotifications];
}
#endif
//For interactive notification only
- (void)application:(UIApplication *)application handleActionWithIdentifier:(NSString *)identifier forRemoteNotification:(NSDictionary *)userInfo completionHandler:(void(^)())completionHandler
{
    //handle the actions
    if ([identifier isEqualToString:@"declineAction"]){
    }
    else if ([identifier isEqualToString:@"answerAction"]){
    }
}

/*
 * ------------------------------------------------------------------------------------------
 *  BEGIN APNS CODE
 * ------------------------------------------------------------------------------------------
 */

/**
 * Fetch and Format Device Token and Register Important Information to Remote Server
 */
- (void)application:(UIApplication *)application didRegisterForRemoteNotificationsWithDeviceToken:(NSData *)devToken {
    
#if !TARGET_IPHONE_SIMULATOR
    NSLog(@"Innnnnn didRegisterForRemoteNotificationsWithDeviceToken") ;
    // Get Bundle Info for Remote Registration (handy if you have more than one app)
    NSString *appName = [[[NSBundle mainBundle] infoDictionary] objectForKey:@"CFBundleDisplayName"];
    NSString *appVersion = [[[NSBundle mainBundle] infoDictionary] objectForKey:@"CFBundleVersion"];
    
    // Check what Notifications the user has turned on.  We registered for all three, but they may have manually disabled some or all of them.
    NSUInteger rntypes = [[UIApplication sharedApplication] enabledRemoteNotificationTypes];
    
    // Set the defaults to disabled unless we find otherwise...
    NSString *pushBadge = @"disabled";
    NSString *pushAlert = @"disabled";
    NSString *pushSound = @"disabled";
    
    // Check what Registered Types are turned on. This is a bit tricky since if two are enabled, and one is off, it will return a number 2... not telling you which
    // one is actually disabled. So we are literally checking to see if rnTypes matches what is turned on, instead of by number. The "tricky" part is that the
    // single notification types will only match if they are the ONLY one enabled.  Likewise, when we are checking for a pair of notifications, it will only be
    // true if those two notifications are on.  This is why the code is written this way
    if(rntypes == UIRemoteNotificationTypeBadge){
        pushBadge = @"enabled";
    }
    else if(rntypes == UIRemoteNotificationTypeAlert){
        pushAlert = @"enabled";
    }
    else if(rntypes == UIRemoteNotificationTypeSound){
        pushSound = @"enabled";
    }
    else if(rntypes == ( UIRemoteNotificationTypeBadge | UIRemoteNotificationTypeAlert)){
        pushBadge = @"enabled";
        pushAlert = @"enabled";
    }
    else if(rntypes == ( UIRemoteNotificationTypeBadge | UIRemoteNotificationTypeSound)){
        pushBadge = @"enabled";
        pushSound = @"enabled";
    }
    else if(rntypes == ( UIRemoteNotificationTypeAlert | UIRemoteNotificationTypeSound)){
        pushAlert = @"enabled";
        pushSound = @"enabled";
    }
    else if(rntypes == ( UIRemoteNotificationTypeBadge | UIRemoteNotificationTypeAlert | UIRemoteNotificationTypeSound)){
        pushBadge = @"enabled";
        pushAlert = @"enabled";
        pushSound = @"enabled";
    }
    
    // Get the users Device Model, Display Name, Unique ID, Token & Version Number
    UIDevice *dev = [UIDevice currentDevice];
    //	NSString *deviceUuid = dev.uniqueIdentifier;
    //    NSString *deviceName = dev.name;
    //	NSString *deviceModel = dev.model;
    //	NSString *deviceSystemVersion = dev.systemVersion;
    
    // Prepare the Device Token for Registration (remove spaces and < >)
    deviceToken = [[[[devToken description]
                     stringByReplacingOccurrencesOfString:@"<"withString:@""]
                    stringByReplacingOccurrencesOfString:@">" withString:@""]
                   stringByReplacingOccurrencesOfString: @" " withString: @""];
    
    NSLog(@"deviceToken === %@",deviceToken);
    
#endif
}

/**
 * Failed to Register for Remote Notifications
 */
- (void)application:(UIApplication *)application didFailToRegisterForRemoteNotificationsWithError:(NSError *)error {
    
#if !TARGET_IPHONE_SIMULATOR
    
    NSLog(@"Error in registration. Error: %@", error);
    
#endif
}

/**
 * Remote Notification Received while application was open.
 */

- (void)application:(UIApplication *)application didReceiveRemoteNotification:(NSDictionary *)userInfo {
    
#if !TARGET_IPHONE_SIMULATOR
    
    NSString *playSoundOnAlert = [NSString stringWithFormat:@"%@", [[userInfo objectForKey:@"aps"] objectForKey:@"sound"]];

    NSURL *url = [NSURL fileURLWithPath:[NSString stringWithFormat:@"%@/%@",[[NSBundle mainBundle] resourcePath],playSoundOnAlert]];
    
    NSError *error;
    
    AVAudioPlayer *audioPlayer = [[AVAudioPlayer alloc] initWithContentsOfURL:url error:&error];
    audioPlayer.numberOfLoops = 0;
    [audioPlayer play];
    
    NSLog(@"Innnnnn didReceiveRemoteNotification") ;
    NSLog(@"UserInfo: %@", userInfo);
    NSLog(@"remote notification: %@",[NSDate date]);
    NSDictionary *apsInfo = [userInfo objectForKey:@"aps"];
    
    NSString *alert = [apsInfo objectForKey:@"alert"];
    NSLog(@"Received Push Alert: %@", alert);
    
    NSString *sound = [apsInfo objectForKey:@"sound"];
    NSLog(@"Received Push Sound: %@", sound);
    //AudioServicesPlaySystemSound(kSystemSoundID_Vibrate);
    
    //NSString *badge = [apsInfo objectForKey:@"badge"];
    //NSLog(@"Received Push Badge: %@", badge);
    //application.applicationIconBadgeNumber = 0;
    
    
    [DatabaseController insertNotification:apsInfo] ;
    
    NSString *dateString = [apsInfo objectForKey:@"posted_on"];
    NSLog(@"dateString === %@" , dateString) ;
    
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    [dateFormatter setDateFormat:@"yyyy-MM-dd HH:mm:ss"];
    NSDate *date = [dateFormatter dateFromString:dateString];
    
    // Convert date object into desired format
    [dateFormatter setDateFormat:@"MMMM dd, YYYY"];
    NSString *newDateString = [dateFormatter stringFromDate:date];
    NSLog(@"newDateString == %@", newDateString) ;
    
    if([[apsInfo objectForKey:@"alert"] length] > 0) {
        UIAlertView *notificationMsg = [[UIAlertView alloc] initWithTitle:AppName message:[apsInfo objectForKey:@"alert"] delegate:self cancelButtonTitle:NSLocalizedString(@"OK", nil) otherButtonTitles:nil, nil];
        notificationMsg.tag = 2001 ;
        [notificationMsg show] ;
        
    }
    // [self addNotificationData:apsInfo] ;
    
#endif
}


#pragma mark -
#pragma mark - Core Data stack

// Returns the managed object context for the application.
// If the context doesn't already exist, it is created and bound to the persistent store coordinator for the application.
- (NSManagedObjectContext *)managedObjectContext
{
    if (_managedObjectContext != nil) {
        return _managedObjectContext;
    }
    
    NSPersistentStoreCoordinator *coordinator = [self persistentStoreCoordinator];
    if (coordinator != nil) {
        _managedObjectContext = [[NSManagedObjectContext alloc] init];
        [_managedObjectContext setPersistentStoreCoordinator:coordinator];
    }
    return _managedObjectContext;
}

// Returns the managed object model for the application.
// If the model doesn't already exist, it is created from the application's model.
- (NSManagedObjectModel *)managedObjectModel
{
    if (_managedObjectModel != nil) {
        return _managedObjectModel;
    }
    NSURL *modelURL = [[NSBundle mainBundle] URLForResource:@"NotificationData" withExtension:@"momd"];
    _managedObjectModel = [[NSManagedObjectModel alloc] initWithContentsOfURL:modelURL];
    return _managedObjectModel;
}


// Returns the persistent store coordinator for the application.
// If the coordinator doesn't already exist, it is created and the application's store added to it.
- (NSPersistentStoreCoordinator *)persistentStoreCoordinator
{
    if (_persistentStoreCoordinator != nil) {
        return _persistentStoreCoordinator;
    }
    
    NSURL *storeURL = [[self applicationDocumentsDirectory] URLByAppendingPathComponent:@"NotificationData.sqlite"];
    
    NSError *error = nil;
    _persistentStoreCoordinator = [[NSPersistentStoreCoordinator alloc] initWithManagedObjectModel:[self managedObjectModel]];
    if (![_persistentStoreCoordinator addPersistentStoreWithType:NSSQLiteStoreType configuration:nil URL:storeURL options:nil error:&error]) {
        /*
         Replace this implementation with code to handle the error appropriately.
         
         abort() causes the application to generate a crash log and terminate. You should not use this function in a shipping application, although it may be useful during development.
         
         Typical reasons for an error here include:
         * The persistent store is not accessible;
         * The schema for the persistent store is incompatible with current managed object model.
         Check the error message to determine what the actual problem was.
         
         
         If the persistent store is not accessible, there is typically something wrong with the file path. Often, a file URL is pointing into the application's resources directory instead of a writeable directory.
         
         If you encounter schema incompatibility errors during development, you can reduce their frequency by:
         * Simply deleting the existing store:
         [[NSFileManager defaultManager] removeItemAtURL:storeURL error:nil]
         
         * Performing automatic lightweight migration by passing the following dictionary as the options parameter:
         @{NSMigratePersistentStoresAutomaticallyOption:@YES, NSInferMappingModelAutomaticallyOption:@YES}
         
         Lightweight migration will only work for a limited set of schema changes; consult "Core Data Model Versioning and Data Migration Programming Guide" for details.
         
         */
        NSLog(@"Unresolved error %@, %@", error, [error userInfo]);
        abort();
    }
    
    return _persistentStoreCoordinator;
}

// Returns the URL to the application's Documents directory.
- (NSURL *)applicationDocumentsDirectory
{
    return [[[NSFileManager defaultManager] URLsForDirectory:NSDocumentDirectory inDomains:NSUserDomainMask] lastObject];
}

- (void)applicationWillResignActive:(UIApplication *)application
{
    // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
    // Use this method to pause ongoing tasks, disable timers, and throttle down OpenGL ES frame rates. Games should use this method to pause the game.
}

- (void)applicationDidEnterBackground:(UIApplication *)application
{
    // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later. 
    // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
    
    UIApplication *app = [UIApplication sharedApplication];
    UIBackgroundTaskIdentifier bgTask;
    bgTask = [app beginBackgroundTaskWithExpirationHandler:^{
        [app endBackgroundTask:bgTask];
        NSLog(@"Innnnn end background") ;
    }];
}
/*
-(void) getCurrentCoordinatesInBackground {
    NSLog(@"Innnn getCurrentCoordinates") ;
    NSString *URLString =[kBaseURL stringByAppendingString:getcoordinates];
    //NSLog(@"URL=%@",URLString);
    NSUserDefaults *userDefault = [NSUserDefaults standardUserDefaults];
    AFHTTPRequestOperationManager *manager = [AFHTTPRequestOperationManager manager];
    
    NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:[userDefault valueForKey:@"student_id"],@"uid", nil];
    
    // NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:[userDefault valueForKey:@"student_id"],@"uid", nil];
    NSLog(@"parmeters=%@",params);
    
    [manager GET:URLString  parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        // NSLog(@"responseobject=%@ === %d",responseObject, currentHitCount);
        if([responseObject isKindOfClass:[NSDictionary class]]) {
            if ([responseObject valueForKey:@"status"]) {
                if([[responseObject valueForKey:@"data"] isKindOfClass:[NSArray class]] && [[responseObject valueForKey:@"data"] count] > 1) {
                    
                    
                    int currentVal = [[responseObject valueForKey:@"data"] count] - 2 ;
                    
                    if([[responseObject valueForKey:@"data"] count] > 0 && [[responseObject valueForKey:@"data"] count] <= 3) {
                        CLLocationCoordinate2D checkCloseSource ;
                        checkCloseSource.latitude = [[[[responseObject valueForKey:@"data"] objectAtIndex:currentVal] valueForKey:@"latitude"] doubleValue] ;
                        checkCloseSource.longitude = [[[[responseObject valueForKey:@"data"] objectAtIndex:currentVal] valueForKey:@"longitude"] doubleValue] ;
                        
                        float sourceDict = [self getTotalTimeDifferenceForPlace:checkCloseSource destination:sourceCoordinates];
                        
                        float destinationDict = [self getTotalTimeDifferenceForPlace:checkCloseSource destination:destinationCoordinates];
                        if (sourceDict < destinationDict) {
                            towardsValue = @"Destination";
                        }else if(sourceDict > destinationDict){
                            towardsValue = @"Source";
                        }
                        
                    }
                    
                    CLLocationCoordinate2D currentMovingObjCoordinates ;
                    currentMovingObjCoordinates.latitude = [[[[responseObject valueForKey:@"data"] lastObject] valueForKey:@"latitude"] doubleValue] ;
                    currentMovingObjCoordinates.longitude = [[[[responseObject valueForKey:@"data"] lastObject] valueForKey:@"longitude"] doubleValue] ;
                    
                    [movingObjPoint setCoordinate:currentMovingObjCoordinates];
                    
                    if([[responseObject valueForKey:@"data"] count] > 1) {
                        //Draw drive path
                        int arrCount = [[responseObject valueForKey:@"data"] count] ;
                        CLLocationCoordinate2D coordinateArray[2];
                        coordinateArray[0] = CLLocationCoordinate2DMake([[[[responseObject valueForKey:@"data"] objectAtIndex:arrCount-2] valueForKey:@"latitude"] doubleValue], [[[[responseObject valueForKey:@"data"] objectAtIndex:arrCount-2] valueForKey:@"longitude"] doubleValue]);
                        
                        
                        coordinateArray[1] = CLLocationCoordinate2DMake([[[[responseObject valueForKey:@"data"] lastObject] valueForKey:@"latitude"] doubleValue], [[[[responseObject valueForKey:@"data"] lastObject] valueForKey:@"longitude"] doubleValue]);
                        if(![[[[responseObject valueForKey:@"data"] objectAtIndex:arrCount-2] valueForKey:@"latitude"] isEqualToString:[[[responseObject valueForKey:@"data"] lastObject] valueForKey:@"latitude"]] && ![[[[responseObject valueForKey:@"data"] objectAtIndex:arrCount-2] valueForKey:@"longitude"] isEqualToString:[[[responseObject valueForKey:@"data"] lastObject] valueForKey:@"longitude"]]) {
                            if([towardsValue isEqualToString:@"Destination"]) {
                                [self getTotalTimeDifference:coordinateArray[1] destination:destinationCoordinates];
                            }else {
                                [self getTotalTimeDifference:coordinateArray[1] destination:sourceCoordinates];
                            }
                            
                            [self getAddressDetails:coordinateArray[1]] ;
                            
                            self.routeLine = [MKPolyline polylineWithCoordinates:coordinateArray count:2];
                            [self.mapView addOverlay:self.routeLine];
                        }
                    }
                    
                    
                    
                    if(distanceRemain <= 0.1) {
                        [getCoordinatesTimer invalidate];
                        getCoordinatesTimer = nil ;
                        if([towardsValue isEqualToString:@"Destination"]) {
                            [self.toFromTxt setText:@"Student Reached Home"];
                        }else {
                            [self.toFromTxt setText:@"Student Reached School"];
                        }
                        [self reachedDestination] ;
                    }
                    //Draw drive path
                }
                currentHitCount = currentHitCount + 1 ;
            }else{
                
            }
        }else{
            
        }
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [MBProgressHUD hideAllHUDsForView:self.view animated:YES];
        NSLog(@"Error: %@", error);
        NSString *errmsg=[error.userInfo valueForKey:@"NSLocalizedDescription"];
        UIAlertView *connectionErrMsg = [[UIAlertView alloc] initWithTitle:AppName message:errmsg delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil, nil];
        //_nocarlbl.text=errmsg;
        //[connectionErrMsg show];
        
    }];
    
}

-(void) reachedDestination {
    
    //[MBProgressHUD showHUDAddedTo:self.view animated:YES];
    NSUserDefaults *userDefault = [NSUserDefaults standardUserDefaults];
    NSString *URLString =[kBaseURL stringByAppendingString:reacheddestination];
    NSLog(@"URL=%@",URLString);
    NSString *reachedat = @"";
    if([towardsValue isEqualToString:@"Destination"]) {
        reachedat = @"school";
    }else{
        reachedat = @"home";
    }
    
    AFHTTPRequestOperationManager *manager = [AFHTTPRequestOperationManager manager];
    NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:[userDefault valueForKey:@"student_id"],@"uid", reachedat,@"wayto", nil];
    NSLog(@"parmeters=%@",params);
    
    [manager GET:URLString  parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        NSLog(@"responseobject=%@",responseObject);
        if([responseObject isKindOfClass:[NSDictionary class]]) {
            NSString *status=[responseObject valueForKey:@"status"] ;
            if ([status intValue]==1) {
                
                
                
            }else{
                
            }
        }else{
            
        }
        
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [MBProgressHUD hideAllHUDsForView:self.view animated:YES];
        NSLog(@"Error: %@", error);
        
    }];
    
}
*/
- (void)applicationWillEnterForeground:(UIApplication *)application
{
    // Called as part of the transition from the background to the inactive state; here you can undo many of the changes made on entering the background.
}

- (void)applicationDidBecomeActive:(UIApplication *)application
{
    // Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
}

- (void)applicationWillTerminate:(UIApplication *)application
{
    // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
}

@end
