//
//  HomeScreenViewController.h
//  StudentTracker
//
//  Created by Umesh Dhuri on 5/28/15.
//  Copyright (c) 2015 AppKnetics. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <MapKit/MapKit.h>
#import <CoreLocation/CoreLocation.h>
#import "DrawerViewController.h"

@interface HomeScreenViewController : UIViewController <MKMapViewDelegate, MKAnnotation, CCKFNavDrawerDelegate> {
    MKPointAnnotation *sourcePoint, *destinationPoint, *movingObjPoint ;
    NSTimer *getCoordinatesTimer, *drawPathTimer, *arrivingTimer, *boardedBusTimer, *reachedBusTimer ;
    int currentHitCount ;
    MKAnnotationView *currentPointView ;
    
    CLPlacemark *thePlacemark;
    MKRoute *routeDetails;
   // CLLocationCoordinate2D coordinateArray[2];
    MKPolyline *polyLine ;
    CLLocationCoordinate2D destinationCoordinates ;
    CLLocationCoordinate2D sourceCoordinates ;
    CLLocationCoordinate2D currentCoordinatesPoints ;
    BOOL checkNearByLocation ;
    float distanceRemain ;
    NSString *towardsValue ;
    int speedval ;
    float distanceVal ;
    BOOL setCurrentAnnotation ;
    NSMutableArray *userDataCollection ;
}
@property (nonatomic, weak) IBOutlet UISegmentedControl *segmentControl ;
@property (nonatomic, weak) IBOutlet MKMapView *mapView;
@property (nonatomic, retain) MKPolyline *routeLine; //your line
@property (nonatomic, retain) MKPolylineView *routeLineView; //overlay view
@property (strong, nonatomic) sliderDrawe1ViewController *rootNav;
@property (nonatomic, weak) IBOutlet UILabel *estimatesTime ;
@property (nonatomic, weak) IBOutlet UILabel *viaTxt, *toFromTxt ;
- (void)routeDraw:(MKPlacemark *) sourcePlaceMark ;
- (IBAction)drawerclicked:(id)sender;
@end
