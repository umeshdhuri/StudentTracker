//
//  DrawerView.h
//  cabApp
//
//  Created by Umesh Dhuri on 17/02/15.
//  Copyright (c) 2015 Appnetics. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "DrawerViewController.h"


@interface DrawerView : UIView<CCKFNavDrawerDelegate> 


@property (weak, nonatomic) IBOutlet UITableView *drawerTableView;
@property (weak, nonatomic) IBOutlet UILabel *namelbl;
@property (weak, nonatomic) IBOutlet UILabel *mobnumlbl;

@property (weak, nonatomic) IBOutlet UIImageView *arrowImage;
@property (weak, nonatomic) IBOutlet UIImageView *profileImage;
@property (weak, nonatomic) IBOutlet UIView *profileView;
@property (weak, nonatomic) IBOutlet UIButton *logoClick ;

- (IBAction)drawercloseclickes:(id)sender;
-(IBAction)closeDrawer:(id)sender ;
@property (weak, nonatomic) IBOutlet UIButton *srwrclsebtn;


@end
