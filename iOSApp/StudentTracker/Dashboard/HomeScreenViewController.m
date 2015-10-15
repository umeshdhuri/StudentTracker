//
//  HomeScreenViewController.m
//  StudentTracker
//
//  Created by Umesh Dhuri on 5/28/15.
//  Copyright (c) 2015 AppKnetics. All rights reserved.
//

#import "HomeScreenViewController.h"

@interface HomeScreenViewController ()

@end

@implementation HomeScreenViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    setCurrentAnnotation = false ;
    checkNearByLocation = false ;
    
    self.navigationController.navigationBarHidden = YES ;
    
    self.rootNav = (sliderDrawe1ViewController *)self.navigationController;
    [self.rootNav setCCKFNavDrawerDelegate:self];
    
    [self.mapView setDelegate:self];
    
    CGRect mapViewFrame = self.mapView.frame ;
    if(isPhone480) {
        mapViewFrame.size.height = 329 ;
        self.mapView.frame = mapViewFrame ;
    }
    
    currentHitCount = 1 ;
    [self getUserLocationData] ;
    // Do any additional setup after loading the view from its nib.
}

-(void) viewWillDisappear:(BOOL)animated {
    //[getCoordinatesTimer invalidate];
    //getCoordinatesTimer = nil ;
}

-(void) viewDidDisappear:(BOOL)animated {
    
}

-(IBAction)refreshMap:(id)sender {
    currentHitCount = 1 ;
    if ([self.routeLine isKindOfClass:[MKPolyline class]]){
        [getCoordinatesTimer invalidate];
        getCoordinatesTimer = nil ;
        
        [self.mapView removeOverlays:self.mapView.overlays] ;
        [self.mapView removeAnnotation:sourcePoint] ;
        [self.mapView removeAnnotation:destinationPoint] ;
        
    }
    
    [self getUserLocationData] ;
}

-(void) getUserLocationData {
    
    [MBProgressHUD showHUDAddedTo:self.view animated:YES];
    NSUserDefaults *userDefault = [NSUserDefaults standardUserDefaults];
    NSString *URLString =[kBaseURL stringByAppendingString:configurationVal];
    NSLog(@"URL=%@",URLString);
    
    AFHTTPRequestOperationManager *manager = [AFHTTPRequestOperationManager manager];
    NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:[userDefault valueForKey:@"student_id"],@"uid", nil];
    NSLog(@"parmeters=%@",params);
    
    [manager GET:URLString  parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        NSLog(@"responseobject=%@",responseObject);
        if([responseObject isKindOfClass:[NSDictionary class]]) {
            NSString *status=[responseObject valueForKey:@"status"] ;
            if ([responseObject valueForKey:@"status"]) {
                [MBProgressHUD hideAllHUDsForView:self.view animated:YES];
                [userDefault setObject:[[[responseObject valueForKey:@"data"] valueForKey:@"0"] valueForKey:@"value"] forKey:@"schoolLatitude"];
                [userDefault setObject:[[[responseObject valueForKey:@"data"] valueForKey:@"1"] valueForKey:@"value"] forKey:@"schoolLongitude"];
                [userDefault setObject:[[responseObject valueForKey:@"data"] valueForKey:@"user_latitude"] forKey:@"userLatitude"];
                [userDefault setObject:[[responseObject valueForKey:@"data"] valueForKey:@"user_longitude"] forKey:@"userLongitude"];
                
                NSLog(@"Home Location userLatitude=== %@", [userDefault valueForKey:@"userLatitude"]) ;
                NSLog(@"Home Location userLongitude === %@", [userDefault valueForKey:@"userLongitude"]) ;
                NSLog(@"Home Location schoolLatitude=== %@", [userDefault valueForKey:@"schoolLatitude"]) ;
                NSLog(@"Home Location schoolLongitude=== %@", [userDefault valueForKey:@"schoolLongitude"]) ;
                
                [self placeLocationOnMap] ;
                
            }else{
                [MBProgressHUD hideAllHUDsForView:self.view animated:YES];
                MBProgressHUD *hud = [MBProgressHUD showHUDAddedTo:self.view animated:YES];
                hud.mode = MBProgressHUDModeText;
                hud.detailsLabelText = @"”Entered phone number desn't match with our system. If you have any queries please contact to school by clicking on above Contact Us link";
                hud.margin = 10.f;
                hud.yOffset = 200.f;
                hud.removeFromSuperViewOnHide = YES;
                [hud hide:YES afterDelay:7];
            }
        }else{
            [MBProgressHUD hideAllHUDsForView:self.view animated:YES];
            MBProgressHUD *hud = [MBProgressHUD showHUDAddedTo:self.view animated:YES];
            hud.mode = MBProgressHUDModeText;
            hud.detailsLabelText = @"”Sign in failed. Please try again with correct phone number.”";
            hud.margin = 10.f;
            hud.yOffset = 200.f;
            hud.removeFromSuperViewOnHide = YES;
            [hud hide:YES afterDelay:2];
        }
        
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [MBProgressHUD hideAllHUDsForView:self.view animated:YES];
        NSLog(@"Error: %@", error);
        NSString *errmsg=[error.userInfo valueForKey:@"NSLocalizedDescription"];
        UIAlertView *connectionErrMsg = [[UIAlertView alloc] initWithTitle:AppName message:errmsg delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil, nil];
        //_nocarlbl.text=errmsg;
        [connectionErrMsg show];
        
    }];
    
}

-(void) placeLocationOnMap {
    
    NSUserDefaults *userDefault = [NSUserDefaults standardUserDefaults];
    
    [self.mapView removeAnnotation:sourcePoint] ;
    [self.mapView removeAnnotation:destinationPoint] ;
    
    //Source Location Start
    sourceCoordinates.latitude = [[userDefault valueForKey:@"schoolLatitude"] floatValue];
    sourceCoordinates.longitude = [[userDefault valueForKey:@"schoolLongitude"] floatValue] ;
    
    // Add an annotation
    sourcePoint = [[MKPointAnnotation alloc] init];
    sourcePoint.coordinate = sourceCoordinates;
    sourcePoint.title = @"";
    [self.mapView addAnnotation:sourcePoint];
    
    //Source Location End
    
    //Destination Location Start
    destinationCoordinates.latitude = [[userDefault valueForKey:@"userLatitude"] floatValue] ;
    destinationCoordinates.longitude = [[userDefault valueForKey:@"userLongitude"] floatValue] ;
    
    // Add an annotation
    destinationPoint = [[MKPointAnnotation alloc] init];
    destinationPoint.coordinate = destinationCoordinates;
    destinationPoint.title = @"";
    [self.mapView addAnnotation:destinationPoint];
    
    //Get Time and Distance between 2 points
   // [self getTotalTimeDifference:sourceCoordinates destination:destinationCoordinates] ;
    
    //Set zoom lavel
    MKCoordinateRegion Destinationregion = MKCoordinateRegionMakeWithDistance(destinationCoordinates, 3000, 3000);
    [self.mapView setRegion:[self.mapView regionThatFits:Destinationregion] animated:YES];
    [self.mapView selectAnnotation:sourcePoint animated:YES];
    [self.mapView selectAnnotation:destinationPoint animated:YES];
    //Destination Location End
    
    setCurrentAnnotation = false ;
    
    //Show moving object end
    [self.mapView removeAnnotation:movingObjPoint] ;
    
    //Live Test
    [self getCurrentCoordinatesPathDraw] ;
    
    //Demo Code
    //[self getArrvingNotification] ;
    
    NSLog(@"currentHitCount == %d",currentHitCount) ;
    
}

//Demo Code

-(void) getArrvingNotification {
    arrivingTimer = [NSTimer scheduledTimerWithTimeInterval:10.0f target:self selector:@selector(demoGetArrivingNotification) userInfo:nil repeats:NO] ;
}

-(void) demoGetArrivingNotification {
   // NSString *URLString = [NSString stringWithFormat:@"http://118.139.163.225/studenttrack/simpleiOSpush.php?pushtype=0&device_id=%@", appDelegate.deviceToken] ;
    
    NSString *URLString = [NSString stringWithFormat:@"http://zeemountlitera.trackchap.com/simpleiOSpush.php?pushtype=0&device_id=%@", appDelegate.deviceToken] ;
    
    
    //[kBaseURL stringByAppendingString:getcoordinates];
    NSUserDefaults *userDefault = [NSUserDefaults standardUserDefaults];
    AFHTTPRequestOperationManager *manager = [AFHTTPRequestOperationManager manager];
    
    //NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:@"0",@"pushtype", nil];
    //NSLog(@"parmeters=%@",params);
    
    [manager GET:URLString  parameters:nil success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        //[self getUserTravallingRoute] ;
        
    }failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        
        
    }];
    
    [arrivingTimer invalidate] ;
    arrivingTimer = nil ;
    
    boardedBusTimer = [NSTimer scheduledTimerWithTimeInterval:10.0f target:self selector:@selector(getUserTravallingRoute) userInfo:nil repeats:NO] ;
    
}

-(void) getUserTravallingRoute {
    userDataCollection = [[NSMutableArray alloc] init] ;
    
    NSString *URLString = [NSString stringWithFormat:@"%@demotrack", kBaseURL] ;
    
    //NSString *URLString = @"http://118.139.163.225/studenttrack/api/demotrack";
    //NSLog(@"URL=%@",URLString);
    NSUserDefaults *userDefault = [NSUserDefaults standardUserDefaults];
    AFHTTPRequestOperationManager *manager = [AFHTTPRequestOperationManager manager];
    
    // NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:[userDefault valueForKey:@"student_id"],@"uid",[NSString stringWithFormat:@"%d", currentHitCount],@"limit", nil];
    //NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:[userDefault valueForKey:@"student_id"],@"uid", nil];
    //NSLog(@"parmeters=%@",params);
    
    [manager GET:URLString  parameters:nil success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        NSLog(@"responseobject=%@",responseObject);
        if([responseObject isKindOfClass:[NSDictionary class]]) {
            if ([responseObject valueForKey:@"status"]) {
                if([[responseObject valueForKey:@"data"] count] > 0) {
                    userDataCollection = [responseObject valueForKey:@"data"] ;
                    currentHitCount = 1 ;
                    [boardedBusTimer invalidate];
                    boardedBusTimer = nil ;
                    [self getBoardedOntoSchoolBus] ;
                    
                }
            }
        }
    }failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [MBProgressHUD hideAllHUDsForView:self.view animated:YES];
        
        getCoordinatesTimer = [NSTimer scheduledTimerWithTimeInterval:3.0 target:self selector:@selector(drawDemoRoute) userInfo:nil repeats:YES] ;
       // getCoordinatesTimer = [NSTimer scheduledTimerWithTimeInterval:3.0 target:self selector:@selector(getCurrentCoordinates) userInfo:nil repeats:YES] ;
        
        NSLog(@"Error: %@", error);
        NSString *errmsg=[error.userInfo valueForKey:@"NSLocalizedDescription"];
        //UIAlertView *connectionErrMsg = [[UIAlertView alloc] initWithTitle:AppName message:errmsg delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil, nil];
        //_nocarlbl.text=errmsg;
        //[connectionErrMsg show];
        
    }];
     
}

-(void) getBoardedOntoSchoolBus {
   // NSString *URLString =[NSString stringWithFormat:@"http://118.139.163.225/studenttrack/simpleiOSpush.php?pushtype=1&device_id=%@", appDelegate.deviceToken];
    
    NSString *URLString = [NSString stringWithFormat:@"http://zeemountlitera.trackchap.com/simpleiOSpush.php?pushtype=1&device_id=%@", appDelegate.deviceToken] ;
    
    NSUserDefaults *userDefault = [NSUserDefaults standardUserDefaults];
    AFHTTPRequestOperationManager *manager = [AFHTTPRequestOperationManager manager];
    
   // NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:@"1",@"pushtype", nil];
   // NSLog(@"parmeters=%@",params);
    
    [manager GET:URLString  parameters:nil success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
    }failure:^(AFHTTPRequestOperation *operation, NSError *error) {
       // getCoordinatesTimer = [NSTimer scheduledTimerWithTimeInterval:3.0 target:self selector:@selector(drawDemoRoute) userInfo:nil repeats:YES] ;
        
    }];
    
    getCoordinatesTimer = [NSTimer scheduledTimerWithTimeInterval:3.0 target:self selector:@selector(drawDemoRoute) userInfo:nil repeats:YES] ;
}

-(void) drawDemoRoute {
    
        if([userDataCollection isKindOfClass:[NSMutableArray class]] && [userDataCollection count] > 0) {
            
            if(currentHitCount > [userDataCollection count]) {
                currentHitCount = [userDataCollection count] ;
            }
            
            //int currentVal = [[responseObject valueForKey:@"data"] count] - 2 ;
            int currentVal = currentHitCount - 1 ;
            
            if([userDataCollection count] > 0 && currentHitCount <= 3) {
                CLLocationCoordinate2D checkCloseSource ;
                checkCloseSource.latitude = [[[userDataCollection objectAtIndex:currentVal] valueForKey:@"latitude"] doubleValue] ;
                checkCloseSource.longitude = [[[userDataCollection objectAtIndex:currentVal] valueForKey:@"longitude"] doubleValue] ;
                
                float sourceDict = [self getTotalTimeDifferenceForPlace:checkCloseSource destination:sourceCoordinates];
                
                float destinationDict = [self getTotalTimeDifferenceForPlace:checkCloseSource destination:destinationCoordinates];
                
                NSLog(@"sourceDict == %f", sourceDict) ;
                NSLog(@"destinationDict == %f", destinationDict) ;
                
                if (sourceDict < destinationDict) {
                    towardsValue = @"Destination";
                }else if(sourceDict > destinationDict){
                    towardsValue = @"Source";
                }
                
            }
            
            CLLocationCoordinate2D currentMovingObjCoordinates ;
            currentMovingObjCoordinates.latitude = [[[userDataCollection objectAtIndex:currentHitCount] valueForKey:@"latitude"] doubleValue] ;
            currentMovingObjCoordinates.longitude = [[[userDataCollection objectAtIndex:currentHitCount] valueForKey:@"longitude"] doubleValue] ;
            
            //Show moving object
            if(!setCurrentAnnotation) {
                movingObjPoint = [[MKPointAnnotation alloc] init];
                movingObjPoint.coordinate = currentMovingObjCoordinates;
                currentPointView = [[MKAnnotationView alloc] initWithAnnotation:movingObjPoint reuseIdentifier:@"MovingLocation"];
                [self.mapView addAnnotation:currentPointView.annotation];
                setCurrentAnnotation = true ;
            }
            
            [movingObjPoint setCoordinate:currentMovingObjCoordinates];
            
            if(currentHitCount > 1) {
                //Draw drive path
                int arrCount = [userDataCollection count] ;
                CLLocationCoordinate2D coordinateArray[2];
                coordinateArray[0] = CLLocationCoordinate2DMake([[[userDataCollection objectAtIndex:currentVal] valueForKey:@"latitude"] doubleValue], [[[userDataCollection objectAtIndex:currentVal] valueForKey:@"longitude"] doubleValue]);
                
                
                coordinateArray[1] = CLLocationCoordinate2DMake([[[userDataCollection objectAtIndex:currentHitCount] valueForKey:@"latitude"] doubleValue], [[[userDataCollection objectAtIndex:currentHitCount] valueForKey:@"longitude"] doubleValue]);
                
                if(![[[userDataCollection objectAtIndex:currentVal] valueForKey:@"latitude"] isEqualToString:[[userDataCollection objectAtIndex:currentHitCount] valueForKey:@"latitude"]] && ![[[userDataCollection objectAtIndex:currentVal] valueForKey:@"longitude"] isEqualToString:[[userDataCollection objectAtIndex:currentHitCount] valueForKey:@"longitude"]]) {
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
            
            
            NSLog(@"distanceRemain %f", distanceRemain) ;
            if([userDataCollection count] -1  <= currentHitCount) {
           // if(distanceRemain  <= 0.01) {
                [getCoordinatesTimer invalidate];
                getCoordinatesTimer = nil ;
                if([towardsValue isEqualToString:@"Destination"]) {
                    [self.toFromTxt setText:@"Student Reached Home"];
                }else {
                    [self.toFromTxt setText:@"Student Reached School"];
                }
                NSLog(@"Call from getCurrentCoordinates");
                // [self reachedDestination] ;
                [self getReachedSchoolBus] ;
            }
            //Draw drive path
        }
        currentHitCount = currentHitCount + 1 ;
    
}

-(void) getReachedSchoolBus {
   // NSString *URLString =[NSString stringWithFormat:@"http://118.139.163.225/studenttrack/simpleiOSpush.php?pushtype=2&device_id=%@", appDelegate.deviceToken];
     NSString *URLString = [NSString stringWithFormat:@"http://zeemountlitera.trackchap.com/simpleiOSpush.php?pushtype=2&device_id=%@", appDelegate.deviceToken] ;
    
    NSUserDefaults *userDefault = [NSUserDefaults standardUserDefaults];
    AFHTTPRequestOperationManager *manager = [AFHTTPRequestOperationManager manager];
    
   // NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:@"2",@"pushtype", nil];
   // NSLog(@"parmeters=%@",params);
    
    [manager GET:URLString  parameters:nil success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [getCoordinatesTimer invalidate];
        getCoordinatesTimer = nil ;
    }failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        
        
    }];
    
    [getCoordinatesTimer invalidate] ;
    getCoordinatesTimer = nil ;
}

//Demo Code End


-(void) getTotalTimeDifference:(CLLocationCoordinate2D) sourcePoints destination:(CLLocationCoordinate2D) destinationPointVal {
    
    CLLocation *locA = [[CLLocation alloc] initWithLatitude:sourcePoints.latitude longitude:sourcePoints.longitude];
    
    CLLocation *locB = [[CLLocation alloc] initWithLatitude:destinationPointVal.latitude longitude:destinationPointVal.longitude];
    
    CLLocationDistance distance = [locA distanceFromLocation:locB];
    NSLog(@"distance === %f",distance) ;
    
    
    float time = ((distance / 1000) / 30) * 60 ;
    NSLog(@"time === %f",time) ;
    distanceRemain = distance / 1000 ;
    NSLog(@"towardsValue === %@", towardsValue) ;
    if([towardsValue isEqualToString:@"Destination"]) {
        [self.toFromTxt setText:@"School to Home"];
    }else {
        [self.toFromTxt setText:@"Home to School"];
    }
    NSString  *timeStr = [NSString stringWithFormat:@"%.2f min (%.2f km)", time, distance / 1000] ;
    if(time > 0.0) {
        
        NSString *timeValue = [NSString stringWithFormat:@"%.2f", time];
        NSArray *timeList = [timeValue componentsSeparatedByString:@"."];
        if([timeList count] >= 2) {
            NSString *secVal = [timeList objectAtIndex:1] ;
            if([[timeList objectAtIndex:1] integerValue] > 60) {
                secVal = @"60" ;
            }
            timeStr = [NSString stringWithFormat:@"%@ min %@ sec (%.2f km)", [timeList objectAtIndex:0], secVal, distance / 1000] ;
        }
    }
    
    [self.estimatesTime setText:timeStr];
    
}

-(float) getTotalTimeDifferenceForPlace:(CLLocationCoordinate2D) sourcePoints destination:(CLLocationCoordinate2D) destinationPointVal {
    
    CLLocation *locA = [[CLLocation alloc] initWithLatitude:sourcePoints.latitude longitude:sourcePoints.longitude];
    
    CLLocation *locB = [[CLLocation alloc] initWithLatitude:destinationPointVal.latitude longitude:destinationPointVal.longitude];
    
    CLLocationDistance distance = [locA distanceFromLocation:locB];
    NSLog(@"distance === %f",distance) ;
    
    return distanceRemain = distance / 1000 ;
}

-(void) getCurrentCoordinatesPathDraw {
    NSLog(@"Innnn getCurrentCoordinatesPathDraw") ;
    NSString *URLString =[kBaseURL stringByAppendingString:getcoordinates];
    //NSLog(@"URL=%@",URLString);
    NSUserDefaults *userDefault = [NSUserDefaults standardUserDefaults];
    AFHTTPRequestOperationManager *manager = [AFHTTPRequestOperationManager manager];
    
   // NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:[userDefault valueForKey:@"student_id"],@"uid",[NSString stringWithFormat:@"%d", currentHitCount],@"limit", nil];
    NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:[userDefault valueForKey:@"student_id"],@"uid", nil];
    NSLog(@"parmeters=%@",params);
    
    getCoordinatesTimer = [NSTimer scheduledTimerWithTimeInterval:3.0 target:self selector:@selector(getCurrentCoordinates) userInfo:nil repeats:YES] ;
    
   
    [manager GET:URLString  parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        NSLog(@"responseobject=%@",responseObject);
        if([responseObject isKindOfClass:[NSDictionary class]]) {
            if ([responseObject valueForKey:@"status"]) {
                if([[responseObject valueForKey:@"data"] isKindOfClass:[NSArray class]] && [[responseObject valueForKey:@"data"] count] > 0) {
                    
                    CLLocationCoordinate2D currentMovingObjCoordinates ;
                    currentMovingObjCoordinates.latitude = [[[[responseObject valueForKey:@"data"] lastObject] valueForKey:@"latitude"] doubleValue] ;
                    currentMovingObjCoordinates.longitude = [[[[responseObject valueForKey:@"data"] lastObject] valueForKey:@"longitude"] doubleValue] ;
                    
                    //Show moving object
                    if(!setCurrentAnnotation) {
                        movingObjPoint = [[MKPointAnnotation alloc] init];
                        movingObjPoint.coordinate = currentMovingObjCoordinates;
                        currentPointView = [[MKAnnotationView alloc] initWithAnnotation:movingObjPoint reuseIdentifier:@"MovingLocation"];
                        [self.mapView addAnnotation:currentPointView.annotation];
                        setCurrentAnnotation = true ;
                    }
                    [movingObjPoint setCoordinate:currentMovingObjCoordinates];
                    
                    for (int i = 1; i < [[responseObject valueForKey:@"data"] count]; i++) {
                        
                        if([[responseObject valueForKey:@"data"] count] > 0 && i <= 2) {
                            CLLocationCoordinate2D checkCloseSource ;
                            checkCloseSource.latitude = [[[[responseObject valueForKey:@"data"] objectAtIndex:i] valueForKey:@"latitude"] doubleValue] ;
                            checkCloseSource.longitude = [[[[responseObject valueForKey:@"data"] objectAtIndex:i] valueForKey:@"longitude"] doubleValue] ;
                            
                            float sourceDict = [self getTotalTimeDifferenceForPlace:checkCloseSource destination:sourceCoordinates];
                            
                            float destinationDict = [self getTotalTimeDifferenceForPlace:checkCloseSource destination:destinationCoordinates];
                           // NSLog(@"sourceDict === %f", sourceDict) ;
                           // NSLog(@"sourceDict === %f", destinationDict) ;
                            if (sourceDict < destinationDict) {
                                towardsValue = @"Destination";
                            }else if(sourceDict > destinationDict){
                                towardsValue = @"Source";
                            }
                            
                        }
                        
                        if([[responseObject valueForKey:@"data"] count] > 1 && i > 0) {
                          
                            NSLog(@"count === %d", [[responseObject valueForKey:@"data"] count]) ;
                            NSLog(@"i value === %d", i) ;
                        CLLocationCoordinate2D coordinateArray[2];
                        coordinateArray[0] = CLLocationCoordinate2DMake([[[[responseObject valueForKey:@"data"] objectAtIndex:i-1] valueForKey:@"latitude"] doubleValue], [[[[responseObject valueForKey:@"data"] objectAtIndex:i-1] valueForKey:@"longitude"] doubleValue]);
                        
                        
                        coordinateArray[1] = CLLocationCoordinate2DMake([[[[responseObject valueForKey:@"data"] objectAtIndex:i] valueForKey:@"latitude"] doubleValue], [[[[responseObject valueForKey:@"data"] objectAtIndex:i] valueForKey:@"longitude"] doubleValue]);
                        
                        if(![[[[responseObject valueForKey:@"data"] objectAtIndex:i-1] valueForKey:@"latitude"] isEqualToString:[[[responseObject valueForKey:@"data"] objectAtIndex:i] valueForKey:@"latitude"]] && ![[[[responseObject valueForKey:@"data"] objectAtIndex:i-1] valueForKey:@"longitude"] isEqualToString:[[[responseObject valueForKey:@"data"] objectAtIndex:i] valueForKey:@"longitude"]]) {
                                if([towardsValue isEqualToString:@"Destination"]) {
                                    [self getTotalTimeDifference:coordinateArray[1] destination:destinationCoordinates];
                                }else{
                                    [self getTotalTimeDifference:coordinateArray[1] destination:sourceCoordinates];
                                }
                                
                                [self getAddressDetails:coordinateArray[1]] ;
                                self.routeLine = [MKPolyline polylineWithCoordinates:coordinateArray count:2];
                                [self.mapView addOverlay:self.routeLine];
                        }
                        }
                    }
                    
                    
                    currentHitCount = [[responseObject valueForKey:@"data"] count] ;
                    NSLog(@"distanceRemain %f", distanceRemain) ;
                    
                    if(distanceRemain <= 0.1) {
                        //[getCoordinatesTimer invalidate];
                        //getCoordinatesTimer = nil ;
                        if([towardsValue isEqualToString:@"Destination"]) {
                            [self.toFromTxt setText:@"Student Reached Home"];
                        }else {
                            [self.toFromTxt setText:@"Student Reached School"];
                        }
                        NSLog(@"Call from getCurrentCoordinatesPathDraw");
                       // [self reachedDestination] ;
                    }else{
                        getCoordinatesTimer = [NSTimer scheduledTimerWithTimeInterval:3.0 target:self selector:@selector(getCurrentCoordinates) userInfo:nil repeats:YES] ;
                    }
                    //Draw drive path
                }else{
                    getCoordinatesTimer = [NSTimer scheduledTimerWithTimeInterval:3.0 target:self selector:@selector(getCurrentCoordinates) userInfo:nil repeats:YES] ;
                }
                
            }else{
               getCoordinatesTimer = [NSTimer scheduledTimerWithTimeInterval:3.0 target:self selector:@selector(getCurrentCoordinates) userInfo:nil repeats:YES] ;
            }
        }else{
            getCoordinatesTimer = [NSTimer scheduledTimerWithTimeInterval:3.0 target:self selector:@selector(getCurrentCoordinates) userInfo:nil repeats:YES] ;
        }
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [MBProgressHUD hideAllHUDsForView:self.view animated:YES];
        
        getCoordinatesTimer = [NSTimer scheduledTimerWithTimeInterval:3.0 target:self selector:@selector(getCurrentCoordinates) userInfo:nil repeats:YES] ;
        
        NSLog(@"Error: %@", error);
        NSString *errmsg=[error.userInfo valueForKey:@"NSLocalizedDescription"];
        //UIAlertView *connectionErrMsg = [[UIAlertView alloc] initWithTitle:AppName message:errmsg delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil, nil];
        //_nocarlbl.text=errmsg;
        //[connectionErrMsg show];
        
    }];
    
}

-(void) getCurrentCoordinates {
    NSLog(@"Innnn getCurrentCoordinates") ;
    NSString *URLString =[kBaseURL stringByAppendingString:getcoordinates];
    //NSLog(@"URL=%@",URLString);
    NSUserDefaults *userDefault = [NSUserDefaults standardUserDefaults];
    AFHTTPRequestOperationManager *manager = [AFHTTPRequestOperationManager manager];
    
  // NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:[userDefault valueForKey:@"student_id"],@"uid",[NSString stringWithFormat:@"%d", currentHitCount],@"limit", nil];
    
     NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:[userDefault valueForKey:@"student_id"],@"uid", nil];
    NSLog(@"parmeters=%@",params);
    
    [manager GET:URLString  parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
       // NSLog(@"responseobject=%@ === %d",responseObject, currentHitCount);
        NSLog(@"currentHitCount === %d", currentHitCount) ;
        if([responseObject isKindOfClass:[NSDictionary class]]) {
            if ([responseObject valueForKey:@"status"]) {
                if([[responseObject valueForKey:@"data"] isKindOfClass:[NSArray class]] && [[responseObject valueForKey:@"data"] count] > 1) {
                    
                    if(currentHitCount > [[responseObject valueForKey:@"data"] count]) {
                        currentHitCount = [[responseObject valueForKey:@"data"] count] ;
                    }
                    
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
                    
                    //Show moving object
                    if(!setCurrentAnnotation) {
                        movingObjPoint = [[MKPointAnnotation alloc] init];
                        movingObjPoint.coordinate = currentMovingObjCoordinates;
                        currentPointView = [[MKAnnotationView alloc] initWithAnnotation:movingObjPoint reuseIdentifier:@"MovingLocation"];
                        [self.mapView addAnnotation:currentPointView.annotation];
                        setCurrentAnnotation = true ;
                    }
                    
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
                    
                    
                    NSLog(@"distanceRemain %f", distanceRemain) ;
                    if(distanceRemain <= 0.1) {
                        //[getCoordinatesTimer invalidate];
                        //getCoordinatesTimer = nil ;
                        if([towardsValue isEqualToString:@"Destination"]) {
                            [self.toFromTxt setText:@"Student Reached Home"];
                        }else {
                            [self.toFromTxt setText:@"Student Reached School"];
                        }
                        NSLog(@"Call from getCurrentCoordinates");
                      // [self reachedDestination] ;
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
    
    [getCoordinatesTimer invalidate];
    getCoordinatesTimer = nil ;
    //[MBProgressHUD showHUDAddedTo:self.view animated:YES];
    NSUserDefaults *userDefault = [NSUserDefaults standardUserDefaults];
    NSString *URLString =[kBaseURL stringByAppendingString:reacheddestination];
    NSLog(@"URL=%@",URLString);
    NSString *reachedat = @"";
    if([towardsValue isEqualToString:@"Destination"]) {
       reachedat = @"home";
    }else{
        reachedat = @"school";
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

-(void) getAddressDetails:(CLLocationCoordinate2D) currentLocation {
    
    CLGeocoder *ceo = [[CLGeocoder alloc]init];
    CLLocation *currentLocationVal = [[CLLocation alloc]initWithLatitude:currentLocation.latitude longitude:currentLocation.longitude]; //insert your coordinates
    
    [ceo reverseGeocodeLocation:currentLocationVal
              completionHandler:^(NSArray *placemarks, NSError *error) {
                  
                  CLPlacemark *placemark = [placemarks objectAtIndex:0];
                  //String to hold address
                  NSString *locatedAt = [[placemark.addressDictionary valueForKey:@"FormattedAddressLines"] componentsJoinedByString:@", "];
                  /*NSLog(@"addressDictionary %@", placemark.addressDictionary);
                  
                  NSLog(@"placemark %@",placemark.region);
                  NSLog(@"placemark %@",placemark.country);  // Give Country Name
                  NSLog(@"placemark %@",placemark.locality); // Extract the city name
                  NSLog(@"location %@",placemark.name);
                  NSLog(@"location %@",placemark.ocean);
                  NSLog(@"location %@",placemark.postalCode);
                  NSLog(@"location %@",placemark.subLocality);
                  
                  NSLog(@"location %@",placemark.location);*/
                  //Print the location to console
                  //NSLog(@"I am currently at %@",locatedAt);
                  
                  [self.viaTxt setText:[NSString stringWithFormat:@"via : %@", placemark.name]] ;
                  
              }
     ];
    
}

- (CLLocationCoordinate2D)coordinateWithLocation:(NSDictionary*)location
{
    double latitude = [[location objectForKey:@"lat"] doubleValue];
    double longitude = [[location objectForKey:@"lng"] doubleValue];
    
    return CLLocationCoordinate2DMake(latitude, longitude);
}

-(IBAction)getsegmentClick:(id)sender {
    
    switch (((UISegmentedControl *) sender).selectedSegmentIndex) {
        case 0:
            self.mapView.mapType = MKMapTypeStandard;
            break;
        case 1:
            self.mapView.mapType = MKMapTypeSatellite;
            break;
        case 2:
            self.mapView.mapType = MKMapTypeHybrid;
            break;
            
        default:
            break;
    }
    
}

- (MKOverlayView *)mapView:(MKMapView *)mapView viewForOverlay:(id <MKOverlay>)overlay
{
    MKPolylineView *polylineView = [[MKPolylineView alloc] initWithPolyline:overlay];
    polylineView.strokeColor = [UIColor blueColor];
    polylineView.lineWidth = 10;
    
    return polylineView;
}


- (MKAnnotationView *)mapView:(MKMapView *)mapView viewForAnnotation:(id <MKAnnotation>)annotation
{
    static NSString *annotaionIdentifier=@"MovingLocation";
    MKAnnotationView *aView=(MKAnnotationView*)[mapView dequeueReusableAnnotationViewWithIdentifier:annotaionIdentifier ];
    
    if(aView==nil && annotation == sourcePoint) {
        aView=[[MKAnnotationView alloc]initWithAnnotation:annotation reuseIdentifier:annotaionIdentifier];
        //aView.pinColor = MKPinAnnotationColorRed;
        aView.rightCalloutAccessoryView = [UIButton buttonWithType:UIButtonTypeDetailDisclosure];
        aView.image=[UIImage imageNamed:@"SchoolPingIcon.png"];
        // aView.animatesDrop=FALSE;
        aView.canShowCallout = YES;
    }if(aView==nil && annotation == destinationPoint) {
        aView=[[MKAnnotationView alloc]initWithAnnotation:annotation reuseIdentifier:annotaionIdentifier];
        //aView.pinColor = MKPinAnnotationColorRed;
        aView.rightCalloutAccessoryView = [UIButton buttonWithType:UIButtonTypeDetailDisclosure];
        aView.image=[UIImage imageNamed:@"HomePinIcon.png"];
        // aView.animatesDrop=FALSE;
        aView.canShowCallout = YES;
    }else if (aView==nil && annotation == movingObjPoint) {
        
        aView=[[MKAnnotationView alloc]initWithAnnotation:annotation reuseIdentifier:annotaionIdentifier];
        //aView.pinColor = MKPinAnnotationColorRed;
        aView.rightCalloutAccessoryView = [UIButton buttonWithType:UIButtonTypeDetailDisclosure];
        aView.image=[UIImage imageNamed:@"movingNavigation.gif"];
       // aView.animatesDrop=FALSE;
        aView.canShowCallout = YES;
        //aView.calloutOffset = CGPointMake(-5, 5);
    }
    
    return aView;
}

-(void) removeAnnotationValue:(id) sender {
    //[self.mapView removeAnnotation:sourcePoint] ;
    //[self.mapView removeAnnotation:destinationPoint] ;
}

- (IBAction)drawerclicked:(id)sender {
    
    [self.rootNav drawerToggle];
    
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
