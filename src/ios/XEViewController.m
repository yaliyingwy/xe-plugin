//
//  XEViewController.m
//  xebest-base
//
//  Created by ywen on 15/7/20.
//
//

#import "XEViewController.h"

@interface XEViewController ()

@end

@implementation XEViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    
    self.webView.delegate = self;
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(void)setXeLoading:(id<LoadingProtocol>)xeLoading {
    XEPlugin *xePlugin = [self getCommandInstance:@"XEPlugin"];
    xePlugin.loading = xeLoading;
}

-(void)setXeToast:(id<ToastProtocol>)xeToast {
    XEPlugin *xePlugin = [self getCommandInstance:@"XEPlugin"];
    xePlugin.toast = xeToast;
}


-(void)commonCommand:(NSArray *)params {
#ifdef DEBUG
    NSLog(@"call from js %@", params);
#endif
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
