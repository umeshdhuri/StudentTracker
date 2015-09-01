//
//  sliderDrawe1ViewController.h
//  cabApp
//
//  Created by Umesh Dhuri on 17/02/15.
//  Copyright (c) 2015 Appnetics. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "sliderDrawer1Cell.h"

@protocol CCKFNavDrawerDelegate <NSObject>
@required
- (void)CCKFNavDrawerSelection:(NSInteger)selectionIndex;
@end

@interface sliderDrawe1ViewController : UINavigationController<UIGestureRecognizerDelegate, UITableViewDataSource, UITableViewDelegate, CCKFNavDrawerDelegate>

@property (nonatomic, strong) UIPanGestureRecognizer *pan_gr;
@property (weak, nonatomic)id<CCKFNavDrawerDelegate> CCKFNavDrawerDelegate;

@property(nonatomic,retain)NSMutableArray *tblData;
@property(nonatomic,retain)NSMutableArray *drawerImgLeft;
@property(nonatomic,retain)NSMutableArray *drawerImgRight;

-(IBAction)closeDrawer:(id)sender ;
- (void)drawerToggle;

@end




