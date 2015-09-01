//
//  CurrentAdressScreenViewController.h
//  StudentTracker
//
//  Created by AppKnetics on 21/05/15.
//  Copyright (c) 2015 AppKnetics. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <MapKit/MapKit.h>
#import <CoreLocation/CoreLocation.h>


@interface CurrentAdressScreenViewController : UIViewController<CCKFNavDrawerDelegate,CLLocationManagerDelegate,MKMapViewDelegate,MKAnnotation,UITextViewDelegate, UIAlertViewDelegate>
{
    CLLocationManager *locationManager;
    BOOL moved ;
    MKPointAnnotation *destinationPoint ;
    CLLocationCoordinate2D destinationCoordinates ;
    NSString *locatedAt ;
    UIAlertView *foundLocation ;
}

- (IBAction)drawerclicked:(id)sender;
@property (strong, nonatomic) CLLocationManager *locationManager;
@property (nonatomic, weak) IBOutlet UILabel *userAddress ;
@property (strong, nonatomic) IBOutlet MKMapView *mymapview;
@property (strong, nonatomic) IBOutlet UISlider *slider;
@property (strong, nonatomic) IBOutlet UITextView *adresstv;

@end
