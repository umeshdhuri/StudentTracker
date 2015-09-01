//
//  AttendanceSheetViewController.h
//  StudentTracker
//
//  Created by Umesh Dhuri on 6/29/15.
//  Copyright (c) 2015 AppKnetics. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "CKCalendarView.h"
#import "DrawerViewController.h"

@interface AttendanceSheetViewController : UIViewController <CKCalendarDelegate, CCKFNavDrawerDelegate>

@property (nonatomic, weak) IBOutlet UILabel *presentLbl, *absentLbl, *holidayLbl, *weeklyHolidayLbl, *naLbl ;

@property (nonatomic, weak) IBOutlet UILabel *presentLbl1, *absentLbl1, *holidayLbl1, *weeklyHolidayLbl1, *naLbl1 ;

@property (strong, nonatomic) sliderDrawe1ViewController *rootNav;
@end
