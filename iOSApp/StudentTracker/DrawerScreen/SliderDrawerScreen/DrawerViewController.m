//
//  sliderDrawe1ViewController.m
//  cabApp
//
//  Created by Umesh Dhuri on 17/02/15.
//  Copyright (c) 2015 Appnetics. All rights reserved.
//

#import "DrawerViewController.h"
#import "DrawerView.h"
#import "sliderDrawer1Cell.h"
#import "CurrentAdressScreenViewController.h"
#import "NotificationsviewViewController.h"
#import "HomeScreenViewController.h"
#import "SignInScreenViewController.h"
#import "ContactUsViewController.h"
#import "TakeATourViewController.h"
#import "AttendanceSheetViewController.h"

#define SHAWDOW_ALPHA 0.5
#define MENU_DURATION 0.3
#define MENU_TRIGGER_VELOCITY 350

@interface sliderDrawe1ViewController ()

{
    NSMutableArray *cellclkimages;
}

@property (nonatomic) BOOL isOpen;
@property (nonatomic) float meunHeight;
@property (nonatomic) float menuWidth;
@property (nonatomic) CGRect outFrame;
@property (nonatomic) CGRect inFrame;
@property (strong, nonatomic) UIView *shawdowView;
@property (strong, nonatomic) DrawerView *drawerView;

@end

@implementation sliderDrawe1ViewController

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
	// Do any additional setup after loading the view.
    
    [self setUpDrawer];
  

    self.navigationBarHidden=NO;
    [self.drawerView.logoClick addTarget:self action:@selector(closethedrwerclick) forControlEvents:UIControlEventTouchUpInside];
    
    [self.drawerView.srwrclsebtn addTarget:self action:@selector(closethedrwerclick) forControlEvents:UIControlEventTouchUpInside];
    
   // _tblData=[[NSMutableArray alloc]initWithObjects:@"HOME",@"NOTIFICATIONS",@"ATTENDANCE ", @"EDIT LOCATIONS",@"CONTACT US", @"TAKE A TOUR", @"LOGOUT", nil];
     _tblData=[[NSMutableArray alloc]initWithObjects:@"HOME",@"NOTIFICATIONS",@"CONTACT US", @"TAKE A TOUR", @"LOGOUT", nil];
    
   // _drawerImgLeft=[[NSMutableArray alloc]initWithObjects:@"ic_side_bar_book_ride.png",@"ic_menu_rides.png",@"ic_side_bar_ola_money.png",@"ic_side_bar_invite.png",@"ic_side_bar_rate_xhdpi.png",@"",
                   //@"ic_side_bar_report.png",@"", nil ];
    
    cellclkimages=[[NSMutableArray alloc]initWithObjects:@"ic_side_bar_book_ride_selected",@"ic_menu_rides_selected",@"ic_side_bar_ola_money_selected",@"ic_side_bar_report_selected", nil];
    
    UITapGestureRecognizer *tap1=[[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(singleTapAction:)];
    tap1.numberOfTapsRequired=1;
    self.drawerView.userInteractionEnabled=YES;
   // [self.drawerView addGestureRecognizer:tap1];
    
    
    
   // self.tableView.contentInset = UIEdgeInsetsMake(0, -15, 0, 0);

}
-(void)closethedrwerclick
{
    [self closeNavigationDrawer];
}

- (void)singleTapAction:(UITapGestureRecognizer *)tap {
      [self closeNavigationDrawer];   
}


- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


#pragma mark - push & pop

- (void)pushViewController:(UIViewController *)viewController animated:(BOOL)animated
{
    [super pushViewController:viewController animated:animated];
    
    // disable gesture in next vc
    [self.pan_gr setEnabled:NO];
}

- (UIViewController *)popViewControllerAnimated:(BOOL)animated
{
    UIViewController *vc = [super popViewControllerAnimated:animated];
    
    // enable gesture in root vc
    if ([self.viewControllers count]==1){
        [self.pan_gr setEnabled:YES];
    }
    return vc;
}

- (void)setUpDrawer
{
    self.isOpen = NO;
    
    // load drawer view
    self.drawerView = [[[NSBundle mainBundle] loadNibNamed:@"DrawerView" owner:self options:nil] objectAtIndex:0];
    
    self.meunHeight = self.drawerView.frame.size.height;
    self.menuWidth = self.drawerView.frame.size.width;
    self.outFrame = CGRectMake(-self.menuWidth,0,self.menuWidth,self.meunHeight);
    self.inFrame = CGRectMake (0,0,self.menuWidth,self.meunHeight);
    
    // drawer shawdow and assign its gesture
    self.shawdowView = [[UIView alloc] initWithFrame:self.view.frame];
    self.shawdowView.backgroundColor = [UIColor colorWithRed:0.0 green:0.0 blue:0.0 alpha:0.0];
    self.shawdowView.hidden = YES;
    UITapGestureRecognizer *tapIt = [[UITapGestureRecognizer alloc] initWithTarget:self
                                                                            action:@selector(tapOnShawdow:)];
    [self.shawdowView addGestureRecognizer:tapIt];
    self.shawdowView.translatesAutoresizingMaskIntoConstraints = NO;
    [self.view addSubview:self.shawdowView];
    
    // add drawer view
    [self.drawerView setFrame:self.outFrame];
    [self.view addSubview:self.drawerView];
    
    // drawer list
    [self.drawerView.drawerTableView setContentInset:UIEdgeInsetsMake(0, 0, 0, 0)]; // statuesBarHeight+navBarHeight
    self.drawerView.drawerTableView.dataSource = self;
    self.drawerView.drawerTableView.delegate = self;
    
    // gesture on self.view
    self.pan_gr = [[UIPanGestureRecognizer alloc] initWithTarget:self action:@selector(moveDrawer:)];
    self.pan_gr.maximumNumberOfTouches = 1;
    self.pan_gr.minimumNumberOfTouches = 1;
    //self.pan_gr.delegate = self;
    [self.view addGestureRecognizer:self.pan_gr];
    
    [self.view bringSubviewToFront:self.navigationBar];
    
    //    for (id x in self.view.subviews){
    //        NSLog(@"%@",NSStringFromClass([x class]));
    //    }
}

- (void)drawerToggle
{
    if (!self.isOpen) {
        [self openNavigationDrawer];
    }else{
        [self closeNavigationDrawer];
    }
}



#pragma open and close action

- (void)openNavigationDrawer{
    //    NSLog(@"open x=%f",self.menuView.center.x);
    float duration = MENU_DURATION/self.menuWidth*abs(self.drawerView.center.x)+MENU_DURATION/2; // y=mx+c
    
    // shawdow
    self.shawdowView.hidden = NO;
    [UIView animateWithDuration:duration
                          delay:0
                        options:UIViewAnimationOptionCurveEaseInOut
                     animations:^{
                         self.shawdowView.backgroundColor = [UIColor colorWithRed:0.0 green:0.0 blue:0.0 alpha:SHAWDOW_ALPHA];
                     }
                     completion:nil];
    
    // drawer
    [UIView animateWithDuration:duration
                          delay:0
                        options:UIViewAnimationOptionBeginFromCurrentState
                     animations:^{
                         self.drawerView.frame = self.inFrame;
                     }
                     completion:nil];
    
    self.isOpen= YES;
}

- (void)closeNavigationDrawer{
    //    NSLog(@"close x=%f",self.menuView.center.x);
    float duration = MENU_DURATION/self.menuWidth*abs(self.drawerView.center.x)+MENU_DURATION/2; // y=mx+c
    
    // shawdow
    [UIView animateWithDuration:duration
                          delay:0
                        options:UIViewAnimationOptionCurveEaseInOut
                     animations:^{
                         self.shawdowView.backgroundColor = [UIColor colorWithRed:0.0 green:0.0 blue:0.0 alpha:0.0f];
                     }
                     completion:^(BOOL finished){
                         self.shawdowView.hidden = YES;
                     }];
    
    // drawer
    [UIView animateWithDuration:duration
                          delay:0
                        options:UIViewAnimationOptionBeginFromCurrentState
                     animations:^{
                         self.drawerView.frame = self.outFrame;
                     }
                     completion:nil];
    self.isOpen= NO;
}

#pragma gestures

- (void)tapOnShawdow:(UITapGestureRecognizer *)recognizer {
    [self closeNavigationDrawer];
    [self.navigationBar setHidden:NO];
}

-(void)moveDrawer:(UIPanGestureRecognizer *)recognizer
{
    CGPoint translation = [recognizer translationInView:self.view]; 
    CGPoint velocity = [(UIPanGestureRecognizer*)recognizer velocityInView:self.view];
    //    NSLog(@"velocity x=%f",velocity.x);
    
    if([(UIPanGestureRecognizer*)recognizer state] == UIGestureRecognizerStateBegan) {
        //        NSLog(@"start");
        if ( velocity.x > MENU_TRIGGER_VELOCITY && !self.isOpen) {
            [self openNavigationDrawer];
        }else if (velocity.x < -MENU_TRIGGER_VELOCITY && self.isOpen) {
            [self closeNavigationDrawer];
        }
    }
    
    if([(UIPanGestureRecognizer*)recognizer state] == UIGestureRecognizerStateChanged) {
        //        NSLog(@"changing");
        float movingx = self.drawerView.center.x + translation.x;
        if ( movingx > -self.menuWidth/2 && movingx < self.menuWidth/2){
            
            self.drawerView.center = CGPointMake(movingx, self.drawerView.center.y);
            [recognizer setTranslation:CGPointMake(0,0) inView:self.view];
            
            float changingAlpha = SHAWDOW_ALPHA/self.menuWidth*movingx+SHAWDOW_ALPHA/2; // y=mx+c
            self.shawdowView.hidden = NO;
            self.shawdowView.backgroundColor = [UIColor colorWithRed:0.0 green:0.0 blue:0.0 alpha:changingAlpha];
        }
    }
    
    if([(UIPanGestureRecognizer*)recognizer state] == UIGestureRecognizerStateEnded) {
        //        NSLog(@"end");
        if (self.drawerView.center.x>0){
            [self openNavigationDrawer];
        }else if (self.drawerView.center.x<0){
            [self closeNavigationDrawer];
        }
    }
    
}


- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    // Return the number of sections.
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    // Return the number of rows in the section.
    return [_tblData count];
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
       return 50;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
   
   
    static NSString *CellIdentifier = @"newFriendCell";
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"newFriendCell"];
    
    if (cell == nil) {
        cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleSubtitle reuseIdentifier:CellIdentifier];
    }
    
    tableView.backgroundColor=[UIColor clearColor];
    cell.backgroundColor=[UIColor colorWithRed:28/255.0 green:28/255.0 blue:28/255.0 alpha:1.0];
    cell.textLabel.text=[_tblData objectAtIndex:indexPath.row];
    cell.textLabel.font=[UIFont boldSystemFontOfSize:15];
    cell.textLabel.textColor=[UIColor whiteColor];
    tableView.separatorColor = [UIColor clearColor];
      cell.selectionStyle = UITableViewCellSelectionStyleNone ;
    return cell;
   
}

#pragma mark - Table view delegate

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    
      UITableViewCell *cell =(UITableViewCell *)[tableView cellForRowAtIndexPath:indexPath] ;
            
    cell.textLabel.textColor=[UIColor whiteColor];
      [self CCKFNavDrawerSelection:[indexPath row]];
    self.CCKFNavDrawerDelegate = self ;
    [self closeNavigationDrawer];
    
   }

-(void)CCKFNavDrawerSelection:(NSInteger)selectionIndex
{
    NSString *s = [NSString stringWithFormat:@"%li",(long)selectionIndex];
    NSLog(@"selectedindex=%ld",(long)selectionIndex);
    
    if (selectionIndex==0) {
        NSLog(@"Home");
        HomeScreenViewController *homeview=[[HomeScreenViewController alloc]init];
        [super pushViewController:homeview animated:YES];
    }
    else if (selectionIndex==1)
    {
        NSLog(@"Notifications");
        NotificationsviewViewController *notifnview=[[NotificationsviewViewController alloc]init];
        [super pushViewController:notifnview animated:YES];
    }
    /*else if (selectionIndex==2)
    {
        NSLog(@"Attendance Sheet");
        AttendanceSheetViewController *attendanceView=[[AttendanceSheetViewController alloc]init];
        [super pushViewController:attendanceView animated:YES];
        
        
    }else if (selectionIndex==3)
    {
        NSLog(@"Edit Locations");
        CurrentAdressScreenViewController *currentlocatn=[[CurrentAdressScreenViewController alloc]init];
        [super pushViewController:currentlocatn animated:YES];
    }*/
    else if (selectionIndex==2)
    {
        NSLog(@"Contact Us");
        ContactUsViewController *contactUsView=[[ContactUsViewController alloc]init];
        contactUsView.redirectionType = @"0";
        [super pushViewController:contactUsView animated:YES];
        
    }else if(selectionIndex == 3){
        NSLog(@"Take A Tour");
        TakeATourViewController *takeATourView=[[TakeATourViewController alloc]init];
        //takeATourView.redirectionType = @"1";
        [super pushViewController:takeATourView animated:YES];
    }
    else
    {
        NSUserDefaults *userDefault = [NSUserDefaults standardUserDefaults];
        [userDefault setValue:@"" forKey:@"student_id"];
        [userDefault setValue:@"" forKey:@"student_name"];
        [userDefault setValue:@"" forKey:@"schoolLatitude"];
        [userDefault setValue:@"" forKey:@"schoolLongitude"];
        [userDefault setValue:@"" forKey:@"user_latitude"];
        [userDefault setValue:@"" forKey:@"user_longitude"];
        
        SignInScreenViewController *signInView = [[SignInScreenViewController alloc] init];
        [super pushViewController:signInView animated:YES];
        NSLog(@"Logout");
    }
}
-(void)viewWillAppear:(BOOL)animated
{
    [self.navigationController.navigationBar setHidden:YES];
    
    NSIndexPath *indexPath=[NSIndexPath indexPathForRow:0 inSection:0];
    //[ selectRowAtIndexPath:indexPath animated:YES  scrollPosition:UITableViewScrollPositionBottom]
}
-(void)viewWillDisappear:(BOOL)animated
{
   [self.navigationController.navigationBar setHidden:YES];
}
@end
