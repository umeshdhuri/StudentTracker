//
//  FirstTutorialViewController.m
//  StudentTracker
//
//  Created by AppKnetics on 21/05/15.
//  Copyright (c) 2015 AppKnetics. All rights reserved.
//

#import "FirstTutorialViewController.h"

@interface FirstTutorialViewController ()

@end

@implementation FirstTutorialViewController

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
    
    self.pageControlObj.frame = CGRectMake(self.pageControlObj.frame.origin.x, self.pageControlObj.frame.origin.y, 20, 20) ;
    self.pageControlObj.currentPage = 0 ;
    self.pageControlObj.pageIndicatorTintColor = [UIColor colorWithPatternImage:[UIImage imageNamed:@"inactive_dot_iphone.png"]];
    self.pageControlObj.currentPageIndicatorTintColor = [UIColor colorWithPatternImage:[UIImage imageNamed:@"active_dot_iphone.png"]];
    
    // Do any additional setup after loading the view from its nib.
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
-(void)viewWillAppear:(BOOL)animated
{
    [self.navigationController setNavigationBarHidden:YES];
}
@end
