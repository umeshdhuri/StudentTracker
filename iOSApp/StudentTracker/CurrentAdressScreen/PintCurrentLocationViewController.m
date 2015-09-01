//
//  PintCurrentLocationViewController.m
//  StudentTracker
//
//  Created by Umesh Dhuri on 6/18/15.
//  Copyright (c) 2015 AppKnetics. All rights reserved.
//

#import "PintCurrentLocationViewController.h"
#import "HomeScreenViewController.h"
@interface PintCurrentLocationViewController ()

@end

@implementation PintCurrentLocationViewController

@synthesize userlatitude, userlongitude ;

- (void)viewDidLoad {
    [super viewDidLoad];
    
    CGRect mapViewFrame = self.mymapview.frame ;
    if(isPhone480) {
        mapViewFrame.size.height = 320 ;
        self.mymapview.frame = mapViewFrame ;
    }
    
    
    _mymapview.delegate=self;
    NSUserDefaults *userDefault = [NSUserDefaults standardUserDefaults];
    destinationCoordinates.latitude = [[userDefault valueForKey:@"userLatitude"] floatValue] ;
    destinationCoordinates.longitude = [[userDefault valueForKey:@"userLongitude"] floatValue] ;
    
    // UIAlertView *locationPoint = [[UIAlertView alloc] initWithTitle:AppName message:[NSString stringWithFormat:@"Lat == %f === Long == %f", destinationCoordinates.latitude, destinationCoordinates.longitude] delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil, nil];
    //[locationPoint show] ;
    
    // Add an annotation
    [self.mymapview removeAnnotation:destinationPoint] ;
    destinationPoint = [[MKPointAnnotation alloc] init];
    destinationPoint.coordinate = destinationCoordinates;
    destinationPoint.title = @"";
    [self.mymapview addAnnotation:destinationPoint];
    
    //Set zoom lavel
    MKCoordinateRegion Destinationregion = MKCoordinateRegionMakeWithDistance(destinationCoordinates, 3000, 3000);
    [self.mymapview setRegion:[self.mymapview regionThatFits:Destinationregion] animated:YES];
    [self getAddressDetails:destinationCoordinates] ;
    
    // Do any additional setup after loading the view from its nib.
}

-(void) viewWillDisappear:(BOOL)animated {
    if(self.locationManager) {
        [self.locationManager stopUpdatingLocation] ;
    }
    [super viewWillDisappear:animated];
}

-(void) getAddressDetails:(CLLocationCoordinate2D) currentLocation {
    
    CLGeocoder *ceo = [[CLGeocoder alloc]init];
    CLLocation *currentLocationVal = [[CLLocation alloc]initWithLatitude:currentLocation.latitude longitude:currentLocation.longitude]; //insert your coordinates
    
    [ceo reverseGeocodeLocation:currentLocationVal
              completionHandler:^(NSArray *placemarks, NSError *error) {
                  
                  CLPlacemark *placemark = [placemarks objectAtIndex:0];
                  //String to hold address
                  locatedAt = [[placemark.addressDictionary valueForKey:@"FormattedAddressLines"] componentsJoinedByString:@", "];
                  
                  [self.userAddress setText:[NSString stringWithFormat:@"Address : %@", locatedAt]] ;
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
                  
                  //[self.viaTxt setText:[NSString stringWithFormat:@"via : %@", placemark.name]] ;
                  
              }
     ];
    
}

-(IBAction)getCurrentLocation:(id)sender {
    [MBProgressHUD showHUDAddedTo:self.view animated:YES];
    self.locationManager = [[CLLocationManager alloc] init];
    self.locationManager.delegate = self;
    // Check for iOS 8. Without this guard the code will crash with "unknown selector" on iOS 7.
    if ([self.locationManager respondsToSelector:@selector(requestWhenInUseAuthorization)]) {
#if __IPHONE_OS_VERSION_MAX_ALLOWED >= 80000
        [self.locationManager requestWhenInUseAuthorization];
#endif
    }
    [self.locationManager startUpdatingLocation];
    
}

-(void) alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex {
    if(buttonIndex == 0 && alertView == foundLocation) {
        /*ContactUsViewController *contactView = [[ContactUsViewController alloc] init];
         contactView.redirectionType = @"2";
         contactView.addressVal = self.userAddress.text ;
         [self.navigationController pushViewController:contactView animated:YES];*/
        
        [MBProgressHUD showHUDAddedTo:self.view animated:YES];
        NSUserDefaults *userDefault = [NSUserDefaults standardUserDefaults];
        NSString *URLString =[kBaseURL stringByAppendingString:savelocation];
        NSLog(@"URL=%@",URLString);
        
        AFHTTPRequestOperationManager *manager = [AFHTTPRequestOperationManager manager];
        
        NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:[userDefault valueForKey:@"student_id"],@"uid", [NSString stringWithFormat:@"%f", destinationCoordinates.latitude] ,@"latitude", [NSString stringWithFormat:@"%f", destinationCoordinates.longitude], @"longitude", nil];
        NSLog(@"parmeters=%@",params);
        
        [manager POST:URLString  parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
            
            NSLog(@"responseobject=%@",responseObject);
            if([responseObject isKindOfClass:[NSDictionary class]]) {
                NSString *status=[responseObject valueForKey:@"status"] ;
                if ([responseObject valueForKey:@"status"]) {
                    [MBProgressHUD hideAllHUDsForView:self.view animated:YES];
                    [userDefault setValue:@"1" forKey:@"pinlocation"] ;
                    HomeScreenViewController *homeView = [[HomeScreenViewController alloc] init];
                    [self.navigationController pushViewController:homeView animated:YES] ;
                    
                   // [userDefault setObject:[[responseObject valueForKey:@"data"] valueForKey:@"user_latitude"] forKey:@"userLatitude"];
                   // [userDefault setObject:[[responseObject valueForKey:@"data"] valueForKey:@"user_longitude"] forKey:@"userLongitude"];
                    
                }else{
                    [MBProgressHUD hideAllHUDsForView:self.view animated:YES];
                    MBProgressHUD *hud = [MBProgressHUD showHUDAddedTo:self.view animated:YES];
                    hud.mode = MBProgressHUDModeText;
                    hud.detailsLabelText = @"”Some error occurs. Please try again.”";
                    hud.margin = 10.f;
                    hud.yOffset = 200.f;
                    hud.removeFromSuperViewOnHide = YES;
                    [hud hide:YES afterDelay:7];
                }
            }else{
                [MBProgressHUD hideAllHUDsForView:self.view animated:YES];
                MBProgressHUD *hud = [MBProgressHUD showHUDAddedTo:self.view animated:YES];
                hud.mode = MBProgressHUDModeText;
                hud.detailsLabelText = @"”Some error occurs. Please try again.”";
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
        
    }else if(buttonIndex == 1 && alertView == foundLocation) {
        NSUserDefaults *userDefault = [NSUserDefaults standardUserDefaults];
        destinationCoordinates.latitude = [[userDefault valueForKey:@"userLatitude"] floatValue] ;
        destinationCoordinates.longitude = [[userDefault valueForKey:@"userLongitude"] floatValue] ;
        
        // UIAlertView *locationPoint = [[UIAlertView alloc] initWithTitle:AppName message:[NSString stringWithFormat:@"Lat == %f === Long == %f", destinationCoordinates.latitude, destinationCoordinates.longitude] delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil, nil];
        //[locationPoint show] ;
        
        // Add an annotation
        [self.mymapview removeAnnotation:destinationPoint] ;
        destinationPoint = [[MKPointAnnotation alloc] init];
        destinationPoint.coordinate = destinationCoordinates;
        destinationPoint.title = @"";
        [self.mymapview addAnnotation:destinationPoint];
        
        //Set zoom lavel
        MKCoordinateRegion Destinationregion = MKCoordinateRegionMakeWithDistance(destinationCoordinates, 3000, 3000);
        [self.mymapview setRegion:[self.mymapview regionThatFits:Destinationregion] animated:YES];
        [self getAddressDetails:destinationCoordinates] ;
    }
}

-(void)locationManager:(CLLocationManager *)manager didFailWithError:(NSError *)error{
    
    [MBProgressHUD hideAllHUDsForView:self.view animated:YES];
    UIAlertView *errorAlert = [[UIAlertView alloc]initWithTitle:AppName message:@"There was an error retrieving your location" delegate:nil cancelButtonTitle:@"OK" otherButtonTitles: nil];
    
   // [errorAlert show];
    //[self requestAlwaysAuthorization] ;
    
    NSLog(@"Error: %@",error.description);
    
}

// Location Manager Delegate Methods
- (void)locationManager:(CLLocationManager *)manager didUpdateLocations:(NSArray *)locations
{
    [MBProgressHUD hideAllHUDsForView:self.view animated:YES];
    NSLog(@"locations == %@", locations);
    CLLocation *crnLoc = [locations lastObject];
    NSLog(@"%.8f", crnLoc.coordinate.latitude) ;
    NSLog(@"%.8f", crnLoc.coordinate.longitude) ;
    NSLog(@"%.0f m", crnLoc.altitude) ;
    NSLog(@"%.1f m/s", crnLoc.speed) ;
    
    
    destinationCoordinates.latitude = crnLoc.coordinate.latitude ;
    destinationCoordinates.longitude = crnLoc.coordinate.longitude ;
    
   // UIAlertView *locationPoint = [[UIAlertView alloc] initWithTitle:AppName message:[NSString stringWithFormat:@"Lat == %f === Long == %f", destinationCoordinates.latitude, destinationCoordinates.longitude] delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil, nil];
    //[locationPoint show] ;
    
    // Add an annotation
    [self.mymapview removeAnnotation:destinationPoint] ;
    destinationPoint = [[MKPointAnnotation alloc] init];
    destinationPoint.coordinate = destinationCoordinates;
    destinationPoint.title = @"";
    [self.mymapview addAnnotation:destinationPoint];
    
    //Set zoom lavel
    MKCoordinateRegion Destinationregion = MKCoordinateRegionMakeWithDistance(destinationCoordinates, 3000, 3000);
    [self.mymapview setRegion:[self.mymapview regionThatFits:Destinationregion] animated:YES];
    [self getAddressDetails:destinationCoordinates] ;
    
    NSString *latVal = [NSString stringWithFormat:@"%f", destinationCoordinates.latitude];
    NSString *longVal = [NSString stringWithFormat:@"%f", destinationCoordinates.longitude];
    
    if([latVal length] > 0 && [longVal length] > 0 && [latVal integerValue] > 0 && [longVal integerValue] > 0) {
        foundLocation = [[UIAlertView alloc] initWithTitle:AppName message:@"Are you sure you want to assign your current address location as home location?" delegate:self cancelButtonTitle:@"Yes" otherButtonTitles:@"No", nil];
        [foundLocation show] ;
    }else{
        UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:AppName message:@"Your current loaction not found. Please try again." delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil, nil];
        [alertView show] ;
    }
    
    if(self.locationManager) {
        [self.locationManager stopUpdatingLocation] ;
    }
}

-(IBAction)skipSteps:(id)sender {
    NSUserDefaults *userDefault = [NSUserDefaults standardUserDefaults];
    [userDefault setValue:@"1" forKey:@"pinlocation"] ;
    HomeScreenViewController *homeView = [[HomeScreenViewController alloc] init];
    [self.navigationController pushViewController:homeView animated:YES] ;
    
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
