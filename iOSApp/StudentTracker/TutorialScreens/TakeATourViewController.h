//
//  TakeATourViewController.h
//  StudentTracker
//
//  Created by Umesh Dhuri on 6/15/15.
//  Copyright (c) 2015 AppKnetics. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "SMPageControl.h"
@class SMPageControl ;

@interface TakeATourViewController : UIViewController <UIScrollViewDelegate, CCKFNavDrawerDelegate> {
    SMPageControl *pageControl ;
}
@property (nonatomic,retain) IBOutlet UIScrollView *scrollView ;
@property (nonatomic, weak) IBOutlet UIView *firstView, *secondView, *thirdView, *fourthView ;
@property (strong, nonatomic) sliderDrawe1ViewController *rootNav;
@end
