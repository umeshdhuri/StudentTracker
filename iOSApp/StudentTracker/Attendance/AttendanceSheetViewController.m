//
//  AttendanceSheetViewController.m
//  StudentTracker
//
//  Created by Umesh Dhuri on 6/29/15.
//  Copyright (c) 2015 AppKnetics. All rights reserved.
//

#import "AttendanceSheetViewController.h"

@interface AttendanceSheetViewController ()

@end

@implementation AttendanceSheetViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    //[MBProgressHUD showHUDAddedTo:self.view animated:YES];
    self.navigationController.navigationBarHidden = YES ;
    
    self.rootNav = (sliderDrawe1ViewController *)self.navigationController;
    [self.rootNav setCCKFNavDrawerDelegate:self];
    
    CKCalendarView *calendar = [[CKCalendarView alloc] initWithFrame:CGRectMake(10, 70, 300, 400)];
   // calendar.frame = CGRectMake(10, 64, 300, 480);
    [self.view addSubview:calendar];
    calendar.delegate = self;
    
    [self.presentLbl setHidden:YES];
    [self.absentLbl setHidden:YES];
    [self.holidayLbl setHidden:YES];
    [self.weeklyHolidayLbl setHidden:YES];
    [self.naLbl setHidden:YES];
    
    [self.presentLbl1 setHidden:YES];
    [self.absentLbl1 setHidden:YES];
    [self.holidayLbl1 setHidden:YES];
    [self.weeklyHolidayLbl1 setHidden:YES];
    [self.naLbl1 setHidden:YES];
    
    self.presentLbl.layer.cornerRadius = self.presentLbl.frame.size.height/2;
    self.presentLbl.layer.masksToBounds = YES;
    
    self.absentLbl.layer.cornerRadius = self.absentLbl.frame.size.height/2;
    self.absentLbl.layer.masksToBounds = YES;
    
    self.holidayLbl.layer.cornerRadius = self.holidayLbl.frame.size.height/2;
    self.holidayLbl.layer.masksToBounds = YES;
    
    self.weeklyHolidayLbl.layer.cornerRadius = self.weeklyHolidayLbl.frame.size.height/2;
    self.weeklyHolidayLbl.layer.masksToBounds = YES;
    
    self.naLbl.layer.cornerRadius = self.naLbl.frame.size.height/2;
    self.naLbl.layer.masksToBounds = YES;
    
    //[MBProgressHUD hideAllHUDsForView:self.view animated:YES];
    // Do any additional setup after loading the view from its nib.
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
