//
//  ThirdTutorialViewController.m
//  StudentTracker
//
//  Created by AppKnetics on 21/05/15.
//  Copyright (c) 2015 AppKnetics. All rights reserved.
//

#import "ThirdTutorialViewController.h"
#import "SignInScreenViewController.h"
#import "HomeScreenViewController.h"

@interface ThirdTutorialViewController ()

@end

@implementation ThirdTutorialViewController

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
    NSUserDefaults *userDefault = [NSUserDefaults standardUserDefaults];
    if(userDefault && [[userDefault valueForKey:@"student_id"] length] > 0) {
        [self.loginBtn setTitle:@"Enter into app" forState:UIControlStateNormal] ;
    }else{
        [self.loginBtn setTitle:@"Sign In" forState:UIControlStateNormal] ;
    }
    // Do any additional setup after loading the view from its nib.
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)loginclick:(id)sender {
    NSUserDefaults *userDefault = [NSUserDefaults standardUserDefaults];
    if(userDefault && [[userDefault valueForKey:@"student_id"] length] > 0) {
        HomeScreenViewController *homeView=[[HomeScreenViewController alloc] init];
        [self.navigationController pushViewController:homeView animated:YES];
    }else{
        SignInScreenViewController *signinview=[[SignInScreenViewController alloc]init];
        [self.navigationController pushViewController:signinview animated:YES];
    }
    
}
-(void)viewWillAppear:(BOOL)animated
{
    [self.navigationController setNavigationBarHidden:YES];
}
@end
