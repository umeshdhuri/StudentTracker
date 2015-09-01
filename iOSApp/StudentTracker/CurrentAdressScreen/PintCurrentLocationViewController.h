//
//  PintCurrentLocationViewController.h
//  StudentTracker
//
//  Created by Umesh Dhuri on 6/18/15.
//  Copyright (c) 2015 AppKnetics. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <MapKit/MapKit.h>
#import <CoreLocation/CoreLocation.h>

@interface PintCurrentLocationViewController : UIViewController <MKMapViewDelegate,MKAnnotation>
{
    CLLocationManager *locationManager;
    MKPointAnnotation *destinationPoint ;
    CLLocationCoordinate2D destinationCoordinates ;
    NSString *locatedAt ;
    UIAlertView *foundLocation ;
}
@property (strong, nonatomic) CLLocationManager *locationManager;
@property (nonatomic, weak) IBOutlet UILabel *userAddress ;
@property (strong, nonatomic) IBOutlet MKMapView *mymapview;
@property (strong, nonatomic) NSString *userlatitude, *userlongitude ;
@end
