//
//  TakeATourViewController.m
//  StudentTracker
//
//  Created by Umesh Dhuri on 6/15/15.
//  Copyright (c) 2015 AppKnetics. All rights reserved.
//

#import "TakeATourViewController.h"

@interface TakeATourViewController ()

@end

@implementation TakeATourViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.rootNav = (sliderDrawe1ViewController *)self.navigationController;
    [self.rootNav setCCKFNavDrawerDelegate:self];
    
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
        pageControl.center=CGPointMake(self.view.frame.size.width/2,self.view.frame.size.height-110);
    }else{
        pageControl.center=CGPointMake(self.view.frame.size.width/2,self.view.frame.size.height - 20);
    }
    pageControl.backgroundColor = [UIColor clearColor];
    // Do any additional setup after loading the view from its nib.
}

#pragma mark -
#pragma mark DDPageControl triggered actions
- (void)scrollViewDidScroll:(UIScrollView *)scrollView {
    
    CGFloat pageWidth = self.scrollView.frame.size.width; // you need to have a **iVar** with getter for scrollView
    float fractionalPage = self.scrollView.contentOffset.x / pageWidth;
    NSInteger page = lround(fractionalPage);
    pageControl.currentPage = page; // you need to have a **iVar** with getter for pageControl
    
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
