//
//  TutorialScreenViewController.h
//  StudentTracker
//
//  Created by Umesh Dhuri on 6/3/15.
//  Copyright (c) 2015 AppKnetics. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "SMPageControl.h"

@class SMPageControl ;

@interface TutorialScreenViewController : UIViewController <UIScrollViewDelegate> {
    SMPageControl *pageControl ;
}
@property (nonatomic,retain) IBOutlet UIScrollView *scrollView ;
@property (nonatomic, weak) IBOutlet UIView *firstView, *secondView, *thirdView, *fourthView ;
@property (nonatomic, weak) IBOutlet UIButton *loginBtn ;
@property (nonatomic, weak) IBOutlet UIButton *checkBoxBtn;
@property (nonatomic, readwrite) BOOL dontshowagain ;
@property (nonatomic, weak)  IBOutlet UIImageView *fourthImgView ;
@property (nonatomic, weak) IBOutlet UILabel *dontshowLbl ;
@end
