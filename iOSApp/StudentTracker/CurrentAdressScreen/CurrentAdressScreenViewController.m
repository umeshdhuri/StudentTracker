//
//  CurrentAdressScreenViewController.m
//  StudentTracker
//
//  Created by AppKnetics on 21/05/15.
//  Copyright (c) 2015 AppKnetics. All rights reserved.
//

#import "CurrentAdressScreenViewController.h"
#import "NAPopoverView.h"
#import "ContactUsViewController.h"

#define kOFFSET_FOR_KEYBOARD 80.0

@interface CurrentAdressScreenViewController ()
{
   // CLLocationManager *locationManager;
    CLLocation *currentLocation;
    NAPopoverView *popview;
    NSString *adresspopur;

}




@property (strong, nonatomic) sliderDrawe1ViewController *rootNav;

@end

@implementation CurrentAdressScreenViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    CGRect mapViewFrame = self.mymapview.frame ;
    if(isPhone480) {
        mapViewFrame.size.height = 320 ;
        self.mymapview.frame = mapViewFrame ;
    }
    
    self.rootNav = (sliderDrawe1ViewController *)self.navigationController;
    [self.rootNav setCCKFNavDrawerDelegate:self];

    _mymapview.delegate=self;

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
    NSString *latVal = [NSString stringWithFormat:@"%f", destinationCoordinates.latitude];
    NSString *longVal = [NSString stringWithFormat:@"%f", destinationCoordinates.longitude];
    
    if([latVal length] > 0 && [longVal length] > 0 && [latVal integerValue] > 0 && [longVal integerValue] > 0) {
        foundLocation = [[UIAlertView alloc] initWithTitle:AppName message:@"Are you sure you want to edit your home address?" delegate:self cancelButtonTitle:@"Yes" otherButtonTitles:@"No", nil];
        [foundLocation show] ;
    }else{
        UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:AppName message:@"Your current loaction not found. Please try again." delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil, nil];
        [alertView show] ;
    }
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
            if([[responseObject valueForKey:@"data"] isKindOfClass:[NSDictionary class]]) {
                NSString *status=[responseObject valueForKey:@"status"] ;
                if ([responseObject valueForKey:@"status"]) {
                    [MBProgressHUD hideAllHUDsForView:self.view animated:YES];
                
                    [userDefault setObject:[[responseObject valueForKey:@"data"] valueForKey:@"user_latitude"] forKey:@"userLatitude"];
                    [userDefault setObject:[[responseObject valueForKey:@"data"] valueForKey:@"user_longitude"] forKey:@"userLongitude"];
                    
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
        
    }
}

-(void)locationManager:(CLLocationManager *)manager didFailWithError:(NSError *)error{
    
    UIAlertView *errorAlert = [[UIAlertView alloc]initWithTitle:AppName message:@"There was an error retrieving your location" delegate:nil cancelButtonTitle:@"OK" otherButtonTitles: nil];
    
    [errorAlert show];
    //[self requestAlwaysAuthorization] ;
    
    NSLog(@"Error: %@",error.description);
    
}

// Location Manager Delegate Methods
- (void)locationManager:(CLLocationManager *)manager didUpdateLocations:(NSArray *)locations
{
    NSLog(@"locations == %@", locations);
    CLLocation *crnLoc = [locations lastObject];
    NSLog(@"%.8f", crnLoc.coordinate.latitude) ;
    NSLog(@"%.8f", crnLoc.coordinate.longitude) ;
    NSLog(@"%.0f m", crnLoc.altitude) ;
    NSLog(@"%.1f m/s", crnLoc.speed) ;
    destinationCoordinates.latitude = crnLoc.coordinate.latitude ;
    destinationCoordinates.longitude = crnLoc.coordinate.longitude ;
    
    // Add an annotation
    destinationPoint = [[MKPointAnnotation alloc] init];
    destinationPoint.coordinate = destinationCoordinates;
    destinationPoint.title = @"";
    [self.mymapview addAnnotation:destinationPoint];
    
    //Set zoom lavel
    MKCoordinateRegion Destinationregion = MKCoordinateRegionMakeWithDistance(destinationCoordinates, 3000, 3000);
    [self.mymapview setRegion:[self.mymapview regionThatFits:Destinationregion] animated:YES];
    [self getAddressDetails:destinationCoordinates] ;
    
}

-(void)tapped
{
    [_adresstv resignFirstResponder];
}
- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
- (IBAction)drawerclicked:(id)sender {
    [self.rootNav drawerToggle];

}

- (MKAnnotationView *)mapView:(MKMapView *)mapView viewForAnnotation:(id <MKAnnotation>)annotation
{
    static NSString *annotaionIdentifier=@"MovingLocation";
    MKAnnotationView *aView=(MKAnnotationView*)[mapView dequeueReusableAnnotationViewWithIdentifier:annotaionIdentifier ];
    
    if(aView==nil && annotation == destinationPoint) {
        aView=[[MKAnnotationView alloc]initWithAnnotation:annotation reuseIdentifier:annotaionIdentifier];
        //aView.pinColor = MKPinAnnotationColorRed;
        aView.rightCalloutAccessoryView = [UIButton buttonWithType:UIButtonTypeDetailDisclosure];
        aView.image=[UIImage imageNamed:@"HomePinIcon.png"];
        // aView.animatesDrop=FALSE;
        aView.canShowCallout = YES;
    }
    
    return aView;
}

/*
- (void)locationManager:(CLLocationManager *)manager didUpdateLocations:(NSArray *)locations
{
    currentLocation = [locations objectAtIndex:0];
    [locationManager stopUpdatingLocation];
    CLGeocoder *geocoder = [[CLGeocoder alloc] init] ;
    [geocoder reverseGeocodeLocation:currentLocation completionHandler:^(NSArray *placemarks, NSError *error)
     {
         NSString *CountryArea;
         if (!(error))
         {
             CLPlacemark *placemark = [placemarks objectAtIndex:0];
             NSLog(@"\nCurrent Location Detected\n");
             NSLog(@"placemark %@",placemark);
             NSString *locatedAt = [[placemark.addressDictionary valueForKey:@"FormattedAddressLines"] componentsJoinedByString:@", "];
             NSString *Address = [[NSString alloc]initWithString:locatedAt];
             NSString *Area = [[NSString alloc]initWithString:placemark.locality];
             NSString *Country = [[NSString alloc]initWithString:placemark.country];
             CountryArea = [NSString stringWithFormat:@"%@, %@", Area,Country];
             NSLog(@"adress%@",Address);
             NSString *address=Address;
             [self showonmap:address];
         }
         else
         {
             NSLog(@"Geocode failed with error %@", error);
             NSLog(@"\nCurrent Location Not Detected\n");
             //return;
             CountryArea = NULL;
         }
}];
}
-(void)showonmap:(NSString *)adress
{
    CLGeocoder *geocoder = [[CLGeocoder alloc] init];
    [geocoder geocodeAddressString:adress
                 completionHandler:^(NSArray* placemarks, NSError* error){
                     if (placemarks && placemarks.count > 0) {
                         CLPlacemark *topResult = [placemarks objectAtIndex:0];
                         MKPlacemark *placemark = [[MKPlacemark alloc] initWithPlacemark:topResult];
                         
                         MKCoordinateRegion region = self.mymapview.region;
                         region.center = placemark.region.center;
                         region.span.longitudeDelta /= 8.0;
                         region.span.latitudeDelta /= 8.0;
                         
                         [self.mymapview setRegion:region animated:YES];
                         [self.mymapview addAnnotation:placemark];
                         adresspopur=adress;
                         _adresstv.text=adresspopur;

                        
                     }
                 }
     ];

}
-(void)showpopup:(NSString *)popupadress
{
    popview=[[NAPopoverView alloc] init];
    [popview setFrame:CGRectMake(10,50,300, self.view.frame.size.height-400)];
    popview.center=self.view.center;
    popview.backgroundColor=[UIColor whiteColor];
    
    
    popview.layer.borderColor = [UIColor blackColor].CGColor;
    popview.layer.cornerRadius = 0.5;
    popview.layer.borderWidth = 1.0;
    
    UILabel *adresslabel=[[UILabel alloc]initWithFrame:CGRectMake(20, 10, 250, 100)];
    adresslabel.textAlignment=UITextAlignmentCenter;
    adresslabel.backgroundColor=[UIColor whiteColor];
    adresslabel.numberOfLines=4;
    adresslabel.text=[NSString stringWithFormat:@"Would You like to conform address \"%@\"",popupadress];
  
    UIButton *conformbtn=[[UIButton alloc]initWithFrame:CGRectMake(2, 110, 145, 50)];
    [conformbtn addTarget:self action:@selector(conformclicked) forControlEvents:UIControlEventTouchUpInside];
    conformbtn.backgroundColor=[UIColor blackColor];
    [conformbtn setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    [conformbtn setTitle:@"Confirm" forState:UIControlStateNormal];
    
    UIButton *cancelbtn=[[UIButton alloc]initWithFrame:CGRectMake(152, 110, 145, 50)];
    [cancelbtn addTarget:self action:@selector(cancelclicked) forControlEvents:UIControlEventTouchUpInside];
    cancelbtn.backgroundColor=[UIColor blackColor];
    [cancelbtn setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    [cancelbtn setTitle:@"Cancel" forState:UIControlStateNormal];
    
    
    [popview addSubview:cancelbtn];
    [popview addSubview:adresslabel];
    [popview addSubview:conformbtn];

    [popview show];
}
-(void)conformclicked
{
    [popview dismiss];

}
-(void)cancelclicked
{
    [popview dismiss];

}
- (MKAnnotationView *)mapView:(MKMapView *)mapView viewForAnnotation:(id <MKAnnotation>)annotation
{
    MKAnnotationView *annotationView = [[MKPinAnnotationView alloc] initWithAnnotation:annotation reuseIdentifier:@"loc"];
    annotationView.canShowCallout = YES;
    annotationView.rightCalloutAccessoryView = [UIButton buttonWithType:UIButtonTypeDetailDisclosure];
    
    [mapView addAnnotation:annotation];
    return annotationView;
    //[self showpopup];

}
- (void)mapView:(MKMapView *)mapView didSelectAnnotationView:(MKAnnotationView *)view
{
    
    MKPointAnnotation *testPoint = [[MKPointAnnotation alloc] init];
    testPoint = view.annotation;
    //self.testText.text = testPoint.title;
    // [self showpopup:adresspopur];
    NSLog(@"Selected");
}
-(BOOL)textViewShouldEndEditing:(UITextView *)textView
{
    [textView resignFirstResponder];
    return YES;
}
-(void)textViewDidChange:(UITextView *)textView
{
    [textView resignFirstResponder];
}
-(void)keyboardWillShow {
    // Animate the current view out of the way
    if (self.view.frame.origin.y >= 0)
    {
        [self setViewMovedUp:YES];
    }
    else if (self.view.frame.origin.y < 0)
    {
        [self setViewMovedUp:NO];
    }
}
-(void)keyboardWillHide {
    if (self.view.frame.origin.y >= 0)
    {
        [self setViewMovedUp:YES];
    }
    else if (self.view.frame.origin.y < 0)
    {
        [self setViewMovedUp:NO];
    }
}
-(void)setViewMovedUp:(BOOL)movedUp
{
    [UIView beginAnimations:nil context:NULL];
    [UIView setAnimationDuration:0.3]; // if you want to slide up the view
    
    CGRect rect = self.view.frame;
    if (movedUp)
    {
        // 1. move the view's origin up so that the text field that will be hidden come above the keyboard
        // 2. increase the size of the view so that the area behind the keyboard is covered up.
        rect.origin.y -= kOFFSET_FOR_KEYBOARD;
        rect.size.height += kOFFSET_FOR_KEYBOARD;
    }
    else
    {
        // revert back to the normal state.
        rect.origin.y += kOFFSET_FOR_KEYBOARD;
        rect.size.height -= kOFFSET_FOR_KEYBOARD;
    }
    self.view.frame = rect;
    
    [UIView commitAnimations];
}
-(void)textViewDidBeginEditing:(UITextView *)textView
{
    UITextView *txtFieldObj = (UITextView *) textView ;
    if(!moved) {
        [self animateViewToPosition:self.view directionUP:YES textfield:txtFieldObj];
        moved = YES;
    }

}
-(void)textViewDidEndEditing:(UITextView *)textView
{
    UITextView *txtFieldObj = (UITextView *) textView ;
    if(moved) {
        [self animateViewToPosition:self.view directionUP:NO textfield:txtFieldObj];
    }
    moved = NO;
}
-(void)animateViewToPosition:(UIView *)viewToMove directionUP:(BOOL)up textfield:(UITextView *)textObjVal {
    
    int movementDistance;
    
    if(textObjVal == self.adresstv) {
        movementDistance = -220; // tweak as needed
    }else{
        movementDistance =  0; // tweak as needed
    }
    const float movementDuration = 0.3f; // tweak as needed
    
    int movement = (up ? movementDistance : -movementDistance);
    [UIView beginAnimations: @"animateTextField" context: nil];
    [UIView setAnimationBeginsFromCurrentState: YES];
    [UIView setAnimationDuration: movementDuration];
    viewToMove.frame = CGRectOffset(viewToMove.frame, 0, movement);
    [UIView commitAnimations];
}*/

@end
