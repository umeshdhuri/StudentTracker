//
//  TutirialViewController.m
//  StudentTracker
//
//  Created by AppKnetics on 21/05/15.
//  Copyright (c) 2015 AppKnetics. All rights reserved.
//

#import "TutirialViewController.h"
#import "FirstTutorialViewController.h"
#import "SecondTutorialViewController.h"
#import "ThirdTutorialViewController.h"

@interface TutirialViewController ()

@end

@implementation TutirialViewController

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
    FirstTutorialViewController *fiest=[[FirstTutorialViewController alloc]init];
    SecondTutorialViewController *second=[[SecondTutorialViewController alloc]init];
    ThirdTutorialViewController *third=[[ThirdTutorialViewController alloc]init];
    
    [self addChildViewController:fiest];
    [self addChildViewController:second];
    [self addChildViewController:third];

    
    // Do any additional setup after loading the view from its nib.
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
