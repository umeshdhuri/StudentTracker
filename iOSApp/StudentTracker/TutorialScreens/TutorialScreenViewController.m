//
//  TutorialScreenViewController.m
//  StudentTracker
//
//  Created by Umesh Dhuri on 6/3/15.
//  Copyright (c) 2015 AppKnetics. All rights reserved.
//

#import "TutorialScreenViewController.h"
#define ARC4RANDOM_MAX	0x100000000
#import "HomeScreenViewController.h"
#import "SignInScreenViewController.h"
#import "PintCurrentLocationViewController.h"

@interface TutorialScreenViewController ()

@end

@implementation TutorialScreenViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    self.navigationController.navigationBarHidden = YES ;
    self.dontshowagain = false ;
    
    NSUserDefaults *userDefault = [NSUserDefaults standardUserDefaults];
    if(userDefault && [[userDefault valueForKey:@"student_id"] length] > 0) {
        [self.loginBtn setTitle:@"Enter into app" forState:UIControlStateNormal] ;
    }else{
        [self.loginBtn setTitle:@"Sign In" forState:UIControlStateNormal] ;
    }
    
    int numberOfPages = 4 ;
    // define the scroll view content size and enable paging
    [self.scrollView setPagingEnabled: YES] ;
    
    if(isPhone480) {
        [self.scrollView setContentSize: CGSizeMake(self.scrollView.bounds.size.width * numberOfPages, 460)] ;
    }else{
        [self.scrollView setContentSize: CGSizeMake(self.scrollView.bounds.size.width * numberOfPages, self.scrollView.bounds.size.height - 20)] ;
    }

    //[self.scrollView setContentSize: CGSizeMake(self.scrollView.bounds.size.width * numberOfPages, self.scrollView.bounds.size.height - 20)] ;
    self.scrollView.delegate=self;
    
    
    UILabel *pageLabel ;
    CGRect pageFrame ;
    UIColor *color ;
    char aLetter ;
    for (int i = 0 ; i < numberOfPages ; i++)
    {
        // determine the frame of the current page
        if(isPhone480) {
            pageFrame = CGRectMake(i * self.scrollView.bounds.size.width, 0.0f, self.scrollView.bounds.size.width, 480) ;
        }else{
            pageFrame = CGRectMake(i * self.scrollView.bounds.size.width, 0.0f, self.scrollView.bounds.size.width, self.scrollView.bounds.size.height) ;
        }
        
        // create a page as a simple UILabel
       // pageLabel = [[UILabel alloc] initWithFrame: pageFrame] ;
        
        // add it to the scroll view
        if(i==0) {
            self.firstView.frame = pageFrame ;
            [self.scrollView addSubview: self.firstView] ;
        }else if(i == 1) {
            self.secondView.frame = pageFrame ;
            [self.scrollView addSubview: self.secondView] ;
        }else if(i == 2){
            self.thirdView.frame = pageFrame ;
            [self.scrollView addSubview: self.thirdView] ;
        }else{
            //[self.fourthImgView setImage:[UIImage imageNamed:@""]] ;
            if(isPhone480) {
                CGRect imageViewFrame = self.fourthImgView.frame ;
                imageViewFrame.size.height = self.fourthImgView.frame.size.height - 80 ;
                self.fourthImgView.frame = imageViewFrame ;
            }
            
            [self.checkBoxBtn setImage:[UIImage imageNamed:@"chkbox_off.png"] forState:UIControlStateNormal] ;
            
            self.fourthView.frame = pageFrame ;
            [self.scrollView addSubview: self.fourthView] ;
        }
        
        
        // determine and set its (random) background color
        //color = [UIColor colorWithRed: (CGFloat)arc4random()/ARC4RANDOM_MAX green: (CGFloat)arc4random()/ARC4RANDOM_MAX blue: (CGFloat)arc4random()/ARC4RANDOM_MAX alpha: 1.0f] ;
        //[pageLabel setBackgroundColor: color] ;
        
        // set some label properties
        //[pageLabel setFont: [UIFont boldSystemFontOfSize: 200.0f]] ;
        //[pageLabel setTextAlignment: UITextAlignmentCenter] ;
        //[pageLabel setTextColor: [UIColor darkTextColor]] ;
        
        // set the label's text as the letter corresponding to the current page index
        //aLetter = (char)((i+65)-(i/26)*26) ;	// the capitalized alphabet characters are in the range 65-90
        //[pageLabel setText: [NSString stringWithFormat: @"%c", aLetter]] ;
        
        
    }
    
    pageControl = [[SMPageControl alloc] init];
    // self.pageControl.frame = CGRectMake(0,0,60,30);
    //[pageControl setBackgroundColor:[UIColor redColor]] ;
    pageControl.numberOfPages = 4;
    pageControl.currentPage = 0;
    
    pageControl.pageIndicatorImage = [UIImage imageNamed:@"inactive_dot_iphone.png"];
    pageControl.currentPageIndicatorImage = [UIImage imageNamed:@"active_dot_iphone.png"];
    
    [pageControl sizeToFit];
    [self.view addSubview:pageControl];
    if(isPhone480) {
        pageControl.center=CGPointMake(self.view.frame.size.width/2,self.scrollView.frame.size.height-110);
    }else{
        pageControl.center=CGPointMake(self.view.frame.size.width/2,self.scrollView.frame.size.height-20);
    }
    pageControl.backgroundColor = [UIColor clearColor];
}


#pragma mark -
#pragma mark DDPageControl triggered actions
- (void)scrollViewDidScroll:(UIScrollView *)scrollView {
    
    CGFloat pageWidth = self.scrollView.frame.size.width; // you need to have a **iVar** with getter for scrollView
    float fractionalPage = self.scrollView.contentOffset.x / pageWidth;
    NSInteger page = lround(fractionalPage);
    [pageControl setHidden:(page==3)];
    pageControl.currentPage = page; // you need to have a **iVar** with getter for pageControl
    
}

- (IBAction)loginclick:(id)sender {
    NSUserDefaults *userDefault = [NSUserDefaults standardUserDefaults];
    if(self.dontshowagain) {
        [userDefault setValue:@"on" forKey:@"dontshowagain"] ;
    }else{
        [userDefault setValue:@"off" forKey:@"dontshowagain"] ;
    }
    
    
    if(userDefault && [[userDefault valueForKey:@"student_id"] length] > 0) {
        /*if([userDefault valueForKey:@"pinlocation"] && [[userDefault valueForKey:@"pinlocation"] length] > 0) {
            HomeScreenViewController *homeView=[[HomeScreenViewController alloc]init];
            [self.navigationController pushViewController:homeView animated:YES];
        }else{
            PintCurrentLocationViewController *pinView = [[PintCurrentLocationViewController alloc] init];
            [self.navigationController pushViewController:pinView animated:YES] ;
        }*/
        
        HomeScreenViewController *homeView=[[HomeScreenViewController alloc]init];
        [self.navigationController pushViewController:homeView animated:YES];
    }else{
        SignInScreenViewController *signinview=[[SignInScreenViewController alloc]init];
        [self.navigationController pushViewController:signinview animated:YES];
    }
    
}

-(IBAction)dontshowagainClick:(id)sender {
    UIButton *btn = (UIButton *) sender ;
    UIImage *image1 = [UIImage imageNamed:@"chkbox_off.png"];
    UIImage *image2 = [UIImage imageNamed:@"chkbox_on.png"];
    
    if([btn.imageView.image isEqual:image1]) {
        [self.checkBoxBtn setImage:[UIImage imageNamed:@"chkbox_on.png"] forState:UIControlStateNormal] ;
        NSLog(@"chkbox_off.png") ;
        self.dontshowagain = true ;
        
    }else if([btn.imageView.image isEqual:image2]){
        [self.checkBoxBtn setImage:[UIImage imageNamed:@"chkbox_off.png"] forState:UIControlStateNormal] ;
        self.dontshowagain = false ;
        NSLog(@"chkbox_on.png");
    }else{
        self.dontshowagain = false ;
        NSLog(@"Nothing selected");
    }
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
